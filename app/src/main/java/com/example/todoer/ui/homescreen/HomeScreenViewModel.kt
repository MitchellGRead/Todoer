package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import com.example.todoer.ui.createtodo.CheckList
import com.example.todoer.ui.createtodo.Note
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import com.example.todoer.ui.homescreen.recycler.HomeScreenItem
import com.example.todoer.ui.homescreen.recycler.NoteItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeScreenViewModel @ViewModelInject constructor(
    private val listRepo: TodoListRepo,
    private val noteRepo: TodoNoteRepo,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    @ExperimentalCoroutinesApi
    fun getTodoItems(): LiveData<List<HomeScreenItem>> {
        val todoListFlow = listRepo.observeTodoLists().map { lists ->
            lists.map { ChecklistItem(it) }
        }.flowOn(dispatcher)

        val todoNoteFlow = noteRepo.observeTodoNotes().map { notes ->
            notes.map { NoteItem(it) }
        }.flowOn(dispatcher)

        return combine(todoListFlow, todoNoteFlow) { lists, notes ->
            lists + notes
        }.flowOn(dispatcher).asLiveData()
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

    fun onDeleteTodo(todoId: Long, cardType: TodoType) {
        viewModelScope.launch {
            when (cardType) {
                is CheckList -> listRepo.deleteList(todoId)
                is Note -> noteRepo.deleteNote(todoId)
            }

        }
    }

    fun onRenameTodo(todoId: Long, cardType: TodoType, updatedName: String) {
        viewModelScope.launch {
            when (cardType) {
                is CheckList -> listRepo.updateListName(todoId, updatedName)
                is Note -> noteRepo.updateNoteName(todoId, updatedName)
            }

        }
    }

    /* Navigation Functions */
    fun onFabButtonClicked() {
        _navigateToCreateTodo.value = true
    }

    fun onCreateListNavigated() {
        _navigateToCreateTodo.value = false
    }

    fun onTodoCardClicked(listId: Long, cardType: TodoType, listName: String) {
        _navigateToListDetails.value =
            ListDetailNavArgs(listId, listName)
    }

    fun onTodoListNavigated() {
        _navigateToListDetails.value = null
    }
}
