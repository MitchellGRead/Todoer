package com.example.todoer.ui.listdetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.database.models.TodoItem
import kotlinx.coroutines.launch

class ListDetailsViewModel @ViewModelInject constructor(
    private val repo: ListDetailsRepo
) : ViewModel() {

    val todoItems = repo.fetchTodoItems()

    fun insertTodoItem(listId: Long, itemName: String) {
        viewModelScope.launch {
            repo.insertTodoItem(listId, itemName)
        }
    }
}
