package com.example.todoer.ui.listdetails

import androidx.lifecycle.*
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.models.TodoItem
import com.example.todoer.domain.TodoItemRepo
import com.example.todoer.domain.TodoListRepo
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ListDetailsViewModel @AssistedInject constructor(
    @Assisted private val listId: Long,
    private val itemRepo: TodoItemRepo,
    private val listRepo: TodoListRepo,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItems: LiveData<List<TodoItem>>
        get() = _todoItems

    init {
        observeTodoItems()
    }

    private fun observeTodoItems() {
        viewModelScope.launch {
            itemRepo.observeTodoItems(listId)
                .map { items ->
                    items.sortedBy { it.isComplete }
                }
                .flowOn(dispatcher)
                .collect {
                    _todoItems.value = it
                }
        }
    }

    fun createTodoItem(itemName: String) {
        val defaultItemName = if (itemName.isEmpty()) "New Item" else itemName

        viewModelScope.launch {
            itemRepo.insertTodoItem(listId, defaultItemName)
            updateListTotalItems()
        }
    }

    private suspend fun updateListTotalItems() {
        val listItems = itemRepo.getTodoItems(listId)
        listItems?.let {
            listRepo.updateListTotalTasks(listId, it.size)
        }
    }

    fun onItemCompleted(itemId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            itemRepo.updateItemCompleted(itemId, isChecked)
            updateListCompletedItems()
        }
    }

    private suspend fun updateListCompletedItems() {
        val listItems = itemRepo.getTodoItems(listId)
        listItems?.let { items ->
            val totalCompleted = items.filter { it.isComplete }.size
            listRepo.updateListCompleteTasks(listId, totalCompleted)
        }
    }

    fun onDeleteItem(itemId: Long) {
        viewModelScope.launch {
            itemRepo.deleteTodoItem(itemId)
            updateListCompletedItems()
            updateListTotalItems()
        }
    }

    fun onEditItem(itemId: Long, updatedText: String) {
        viewModelScope.launch {
            itemRepo.updateItemName(itemId, updatedText)
        }
    }

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(listId: Long): ListDetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            listId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(listId) as T
            }
        }
    }
}
