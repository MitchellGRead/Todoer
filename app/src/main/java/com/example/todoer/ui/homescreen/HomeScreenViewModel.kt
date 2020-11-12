package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoListRepo
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeScreenViewModel @ViewModelInject constructor(
    private val repo: TodoListRepo
) : ViewModel() {

    val todoLists = repo.observeTodoLists()

    private val _navigateToCreateList: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToCreateList: LiveData<Boolean>
        get() = _navigateToCreateList

    private val _navigateToTodoList: MutableLiveData<Long?> = MutableLiveData()
    val navigateToTodoList: LiveData<Long?>
        get() = _navigateToTodoList

    init {
        Timber.d("Init HomeScreen ViewModel")
    }

    fun onDeleteList(listId: Long) {
        viewModelScope.launch {
            // TODO("Add in deleting the list items as well")
            repo.deleteList(listId)
        }
    }

    fun onRenameList(listId: Long) {
        TODO()
    }

    fun onShareList(listId: Long) {
        TODO()
    }

    /* Navigation Functions */
    fun onFabButtonClicked() {
        _navigateToCreateList.value = true
    }

    fun onCreateListNavigated() {
        _navigateToCreateList.value = false
    }

    fun onTodoListClicked(listId: Long) {
        _navigateToTodoList.value = listId
    }

    fun onTodoListNavigated() {
        _navigateToTodoList.value = null
    }
}
