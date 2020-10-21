package com.example.todoer.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.database.models.TodoList
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeScreenViewModel @ViewModelInject constructor(
    private val repo: HomeScreenRepo
) : ViewModel() {

//    private val _todoLists: MutableLiveData<List<TodoList>> = MutableLiveData()
//    val todoLists: LiveData<List<TodoList>>
//        get() = _todoLists
    private lateinit var _todoLists: List<TodoList>

    init {
        Timber.d("Init HomeScreen ViewModel")
        updateTodoLists()
    }

    private fun updateTodoLists() {
        viewModelScope.launch {
            _todoLists = repo.fetchTodoLists()
            Timber.d("$_todoLists")
        }
    }
}
