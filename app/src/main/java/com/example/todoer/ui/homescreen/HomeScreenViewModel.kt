package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.navigation.ListDetailNavArgs
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeScreenViewModel @ViewModelInject constructor(
    private val repo: TodoListRepo
) : ViewModel() {

    val todoLists = repo.observeTodoLists()

    private val _navigateToCreateList: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToCreateList: LiveData<Boolean>
        get() = _navigateToCreateList

    private val _navigateToTodoListNav: MutableLiveData<ListDetailNavArgs?> = MutableLiveData()
    val navigateToTodoListNav: LiveData<ListDetailNavArgs?>
        get() = _navigateToTodoListNav

    init {
        Timber.d("Init HomeScreen ViewModel")
    }

    fun onDeleteList(listId: Long) {
        viewModelScope.launch {
            repo.deleteList(listId)
        }
    }

    fun onRenameList(listId: Long, updatedName: String) {
        viewModelScope.launch {
            repo.updateListName(listId, updatedName)
        }
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

    fun onTodoListClicked(listId: Long, listName: String) {
        _navigateToTodoListNav.value =
            ListDetailNavArgs(listId, listName)
    }

    fun onTodoListNavigated() {
        _navigateToTodoListNav.value = null
    }
}
