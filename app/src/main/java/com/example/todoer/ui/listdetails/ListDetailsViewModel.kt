package com.example.todoer.ui.listdetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class ListDetailsViewModel @ViewModelInject constructor(
    private val repo: ListDetailsRepo
) : ViewModel() {

    val todoItems = repo.fetchTodoItems()
}
