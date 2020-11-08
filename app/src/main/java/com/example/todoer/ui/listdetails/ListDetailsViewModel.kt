package com.example.todoer.ui.listdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoer.database.models.TodoItem
import com.example.todoer.database.models.TodoList
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class ListDetailsViewModel @AssistedInject constructor(
    @Assisted private val listId: Long,
    private val repo: ListDetailsRepo
) : ViewModel() {

    val todoItems = repo.observeTodoItems(listId)

    fun insertTodoItem(itemName: String) {
        viewModelScope.launch {
                repo.insertTodoItem(listId, itemName)
                updateListTotalItems()
        }
    }

    private suspend fun updateListTotalItems() {
        val listItems = repo.getTodoItems(listId)
        listItems?.let {
            repo.updateListTotalTasks(listId, it.size)
        }
    }

    fun onItemCompleted(itemId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            repo.updateItemCompleted(itemId, isChecked)
            updateListCompletedItems()
        }
    }

    private suspend fun updateListCompletedItems() {
        val listItems = repo.getTodoItems(listId)
        listItems?.let { items ->
            val totalCompleted = items.filter { it.isComplete }.size
            repo.updateListCompleteTasks(listId, totalCompleted)
        }
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
