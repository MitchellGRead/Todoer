package com.example.todoer.ui.createlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.navigation.ListDetailNavArgs
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateListViewModel @ViewModelInject constructor(
    private val repo: TodoListRepo
) : ViewModel() {

    private val _navigateToTodoList: MutableLiveData<ListDetailNavArgs?> = MutableLiveData()
    val navigateToTodoList: LiveData<ListDetailNavArgs?>
        get() = _navigateToTodoList

    fun onCreateList(listName: String, listType: String) {
        val defaultListName = if (listName.isEmpty()) "New List" else listName

        val type = listType
        viewModelScope.launch {
            val listId = repo.insertList(defaultListName)
            _navigateToTodoList.value = ListDetailNavArgs(
                listId = listId,
                listName = defaultListName
            )
        }
    }

    fun onTodoListNavigated() {
        _navigateToTodoList.value = null
    }
}
