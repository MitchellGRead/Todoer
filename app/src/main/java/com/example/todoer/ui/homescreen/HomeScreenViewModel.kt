package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import com.example.todoer.navigation.NoteDetailNavArgs
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import com.example.todoer.ui.homescreen.recycler.HomeScreenItem
import com.example.todoer.ui.homescreen.recycler.NoteItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

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

    fun onDeleteTodo(homeScreenItem: HomeScreenItem) {
        viewModelScope.launch {
            when (homeScreenItem) {
                is ChecklistItem -> listRepo.deleteList(homeScreenItem.id)
                is NoteItem -> noteRepo.deleteNote(homeScreenItem.id)
            }
        }
    }

    fun onRenameTodo(homeScreenItem: HomeScreenItem, updatedName: String) {
        viewModelScope.launch {
            when (homeScreenItem) {
                is ChecklistItem -> listRepo.updateListName(homeScreenItem.id, updatedName)
                is NoteItem -> noteRepo.updateNoteName(homeScreenItem.id, updatedName)
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

    fun onHomeScreenItemClicked(homeScreenItem: HomeScreenItem) {
        when (homeScreenItem) {
            is ChecklistItem -> _navigateToListDetails.value = ListDetailNavArgs(homeScreenItem.id, homeScreenItem.checkList.listName)
            is NoteItem -> _navigateToNoteDetails.value = NoteDetailNavArgs(homeScreenItem.id, homeScreenItem.note.noteName)
        }
    }

    fun onTodoListNavigated() {
        _navigateToListDetails.value = null
    }

    fun onTodoNoteNavigated() {
        _navigateToNoteDetails.value = null
    }
}
