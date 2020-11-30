package com.example.todoer.ui.createlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.navigation.ListDetailNavArgs
import kotlinx.coroutines.launch

class CreateListViewModel @ViewModelInject constructor(
    private val repo: TodoListRepo
) : ViewModel() {

    private val _navigateToTodoList: MutableLiveData<ListDetailNavArgs?> = MutableLiveData()
    val navigateToTodoList: LiveData<ListDetailNavArgs?>
        get() = _navigateToTodoList

    fun onCreateList(listName: String) {
        viewModelScope.launch {
            val listId = repo.insertList(listName)
            _navigateToTodoList.value = ListDetailNavArgs(
                listId = listId,
                listName = listName
            )
        }
    }

    fun onTodoListNavigated() {
        _navigateToTodoList.value = null
    }
}
