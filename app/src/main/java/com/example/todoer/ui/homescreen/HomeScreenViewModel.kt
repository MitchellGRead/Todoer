package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.todoer.database.models.TodoList
import com.example.todoer.database.models.TodoNote
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import com.example.todoer.ui.homescreen.recycler.HomeScreenItem
import com.example.todoer.ui.homescreen.recycler.NoteItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeScreenViewModel @ViewModelInject constructor(
    private val listRepo: TodoListRepo,
    private val noteRepo: TodoNoteRepo
) : ViewModel() {

    @ExperimentalCoroutinesApi
    fun getTodoItems(): LiveData<List<HomeScreenItem>> {
        val todoListFlow = listRepo.observeTodoLists().map { lists ->
            lists.map { ChecklistItem(it) }
        }
        val todoNoteFlow = noteRepo.observeTodoNotes().map { notes ->
            notes.map { NoteItem(it) }
        }

        return combine(todoListFlow, todoNoteFlow) { lists, notes ->
            lists + notes
        }.asLiveData()
    }

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
