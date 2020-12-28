package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import com.example.todoer.navigation.NoteDetailNavArgs
import com.example.todoer.ui.createtodo.CheckList
import com.example.todoer.ui.createtodo.Note
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.homescreen.recycler.HomeScreenItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class HomeScreenViewModel @ViewModelInject constructor(
    private val listRepo: TodoListRepo,
    private val noteRepo: TodoNoteRepo,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _homeScreenItems: MutableLiveData<List<HomeScreenItem>> = MutableLiveData()
    val homeScreenItems: LiveData<List<HomeScreenItem>>
        get() = _homeScreenItems

    private val _navigateToCreateTodo: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToCreateTodo: LiveData<Boolean>
        get() = _navigateToCreateTodo

    private val _navigateToListDetails: MutableLiveData<ListDetailNavArgs?> = MutableLiveData()
    val navigateToListDetails: LiveData<ListDetailNavArgs?>
        get() = _navigateToListDetails

    private val _navigateToNoteDetails: MutableLiveData<NoteDetailNavArgs?> = MutableLiveData()
    val navigateToNoteDetails: LiveData<NoteDetailNavArgs?>
        get() = _navigateToNoteDetails

    init {
        Timber.d("Init HomeScreen ViewModel")
        getHomeScreenItems()
    }

    private fun getHomeScreenItems() {
        val checklistItems = listRepo.observeChecklistItems()
        val noteItems = noteRepo.observeNoteItems()

        viewModelScope.launch {
            combine(checklistItems, noteItems) { checklists, notes ->
                checklists + notes
            }
                .flowOn(dispatcher)
                .collect{
                    _homeScreenItems.value = it
                }
        }
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

    fun onTodoCardClicked(cardId: Long, cardType: TodoType, cardName: String) {
        when (cardType) {
            is CheckList -> _navigateToListDetails.value = ListDetailNavArgs(cardId, cardName)
            is Note -> _navigateToNoteDetails.value = NoteDetailNavArgs(cardId, cardName)
        }
    }

    fun onTodoListNavigated() {
        _navigateToListDetails.value = null
    }

    fun onTodoNoteNavigated() {
        _navigateToNoteDetails.value = null
    }
}
