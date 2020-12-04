package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeScreenViewModel @ViewModelInject constructor(
    private val listRepo: TodoListRepo,
    private val noteRepo: TodoNoteRepo
) : ViewModel() {

    val todoLists = listRepo.observeTodoLists()

    private val _navigateToCreateTodo: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToCreateTodo: LiveData<Boolean>
        get() = _navigateToCreateTodo

    private val _navigateToListDetails: MutableLiveData<ListDetailNavArgs?> = MutableLiveData()
    val navigateToListDetails: LiveData<ListDetailNavArgs?>
        get() = _navigateToListDetails

    init {
        Timber.d("Init HomeScreen ViewModel")
    }

    fun onDeleteTodo(todoId: Long) {
        viewModelScope.launch {
            listRepo.deleteList(todoId)
        }
    }

    fun onRenameTodo(todoId: Long, updatedName: String) {
        viewModelScope.launch {
            listRepo.updateListName(todoId, updatedName)
        }
    }

    fun onShareList(listId: Long) {
        TODO()
    }

    /* Navigation Functions */
    fun onFabButtonClicked() {
        _navigateToCreateTodo.value = true
    }

    fun onCreateListNavigated() {
        _navigateToCreateTodo.value = false
    }

    fun onTodoListClicked(listId: Long, listName: String) {
        _navigateToListDetails.value =
            ListDetailNavArgs(listId, listName)
    }

    fun onTodoListNavigated() {
        _navigateToListDetails.value = null
    }
}
