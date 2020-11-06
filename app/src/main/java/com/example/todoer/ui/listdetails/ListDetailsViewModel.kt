package com.example.todoer.ui.listdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoer.database.models.TodoList
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class ListDetailsViewModel @AssistedInject constructor(
    @Assisted private val listId: Long,
    private val repo: ListDetailsRepo
) : ViewModel() {

    val todoItems = repo.fetchTodoItems(listId)
    private val todoList = MutableLiveData<TodoList?>()

    init {
        initializeList()
    }

    private fun initializeList() {
        viewModelScope.launch {
            todoList.value = repo.fetchTodoList(listId)
        }
    }

    fun insertTodoItem(itemName: String) {
        viewModelScope.launch {
            todoList.value?.let { todoList ->
                repo.insertTodoItem(todoList, itemName)
                updateListTotalItems(todoList)
            }
        }
    }

    private suspend fun updateListTotalItems(todoList: TodoList) {
        todoItems.value?.let { listItems ->
            val updatedList = todoList.copy(
                totalTasks = listItems.size + 1  // + 1 for adding the item that is being inserted
            )
            repo.updateTodoList(updatedList)
        }
    }

    fun onItemCompleted(itemId: Long, isChecked: Boolean) {

    }

    fun onDeleteItem(itemId: Long) {

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
