package com.example.todoer.ui.listdetails

import androidx.lifecycle.*
import com.example.todoer.base.BaseViewModel
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.models.TodoItem
import com.example.todoer.domain.TodoItemRepo
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.ui.createtodo.CheckList
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class ListDetailsViewModel @AssistedInject constructor(
    @Assisted private val listId: Long,
    private val itemRepo: TodoItemRepo,
    private val listRepo: TodoListRepo,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel<ListAction>() {

    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItems: LiveData<List<TodoItem>>
        get() = _todoItems

    private var deletedItems: List<TodoItem>? = null

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

    fun onSnackbarDismissed() {
        setAction(SnackbarAction(false))
    }

    fun createTodoItem(itemName: String) {
        val defaultItemName = if (itemName.isEmpty()) CheckList.getDefaultItemName() else itemName

        viewModelScope.launch {
            itemRepo.insertTodoItem(listId, defaultItemName)
            updateListTotalItems()
        }
    }

    private suspend fun updateListTotalItems() {
        val listItems = itemRepo.getTodoItems(listId)
        listRepo.updateListTotalTasks(listId, listItems.size)
    }

    fun onItemCompleted(itemId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            itemRepo.updateItemCompleted(itemId, isChecked)
            updateListCompletedItems()
        }
    }

    private suspend fun updateListCompletedItems() {
        val listItems = itemRepo.getTodoItems(listId)
        val totalCompleted = listItems.filter { it.isComplete }.size
        listRepo.updateListCompleteTasks(listId, totalCompleted)
    }

    fun onDeleteItem(item: TodoItem) {
        viewModelScope.launch {
            delete(listOf(item))
        }
    }

    private suspend fun delete(items: List<TodoItem>) {
        if (items.isEmpty()) return

        deletedItems = items
        setAction(SnackbarAction(true))
        itemRepo.deleteItems(items)
        updateListCounts()
    }

    private suspend fun updateListCounts() {
        updateListCompletedItems()
        updateListTotalItems()
    }

    fun deleteFinished() {
        viewModelScope.launch {
            val completed = itemRepo.getTodoItems(listId).filter { it.isComplete }
            delete(completed)
        }
    }

    fun undoDelete() {
        deletedItems?.let { items ->
            viewModelScope.launch {
                itemRepo.insertExisitingTodoItems(items)
                updateListCounts()
            }
        }
    }

    fun onRenameItem(itemId: Long, updatedText: String) {
        viewModelScope.launch {
            itemRepo.updateItemName(itemId, updatedText)
        }
    }

    fun updateEditedDate() {
        val newDate = DateTime()
        listRepo.updateEditDate(listId, newDate)
    }

    /* Sharing logic */
    fun shareTodo() {
        viewModelScope.launch {
            val incompleteItems = itemRepo.getTodoItems(listId).filter { it.isComplete.not() }
            val share = parseItemsToShareString(incompleteItems)
            setAction(ShareAction(share))
        }
    }

    private fun parseItemsToShareString(items: List<TodoItem>): String {
        val titles = items.map { "- ${it.itemName}" }
        return titles.joinToString(separator = "\n")
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
