package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoer.base.BaseViewModel
import com.example.todoer.daggerhilt.DefaultDispatcher
import com.example.todoer.database.models.TodoList
import com.example.todoer.database.models.TodoNote
import com.example.todoer.domain.TodoItemRepo
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import com.example.todoer.navigation.NoteDetailNavArgs
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import com.example.todoer.ui.homescreen.recycler.HomeScreenItem
import com.example.todoer.ui.homescreen.recycler.NoteItem
import com.example.todoer.utils.DateTimeUtils.getDateTimeString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class HomeScreenViewModel @ViewModelInject constructor(
    private val listRepo: TodoListRepo,
    private val noteRepo: TodoNoteRepo,
    private val itemRepo: TodoItemRepo,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel<HomeAction>() {

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

    private val sortChannel = ConflatedBroadcastChannel<Boolean>()
    private var homeScreenItemsJob: Job? = null
    private var lastDeletedCard: HomeScreenItem? = null

    init {
        Timber.d("Init HomeScreen ViewModel")
    }

    /* sortContent drives fetching the home options.*/
    fun sortContent(shouldSort: Boolean) {
        sortChannel.offer(shouldSort)
        getHomeScreenItems()
    }

    private fun getHomeScreenItems() {
        val checklistItems = listRepo.observeTodoLists().toChecklistItems()
        val noteItems = noteRepo.observeTodoNotes().toNoteItems()
        val sortOption = sortChannel.valueOrNull ?: false

        homeScreenItemsJob?.cancel()
        homeScreenItemsJob = viewModelScope.launch {
            combine(
                checklistItems,
                noteItems
            ) { checklists, notes ->
                checklists + notes
            }
                .map { items ->
                    items.sortedWith(getSortComparator(sortOption))
                }
                .flowOn(dispatcher)
                .collect{
                    _homeScreenItems.value = it
                }
        }

    }

    private fun getSortComparator(favouritesToTop: Boolean): Comparator<HomeScreenItem> {
        return if (favouritesToTop) {
            compareByDescending<HomeScreenItem> { it.isFavourited }
                .thenByDescending { it.editedDate }
        } else {
            compareByDescending { it.editedDate }
        }
    }

    /* Conversion Extension Functions */
    private fun Flow<List<TodoList>>.toChecklistItems(): Flow<List<ChecklistItem>> {
        return this.map { todoLists ->
            todoLists.map { todo ->
                val editedString = todo.editedAt.getDateTimeString()
                ChecklistItem(
                    checkList = todo,
                    todoItems = itemRepo.getTodoItems(todo.listId),
                    editedString = editedString
                )
            }
        }
    }

    private fun Flow<List<TodoNote>>.toNoteItems(): Flow<List<NoteItem>> {
        return this.map { todoNotes ->
            todoNotes.map { todo ->
                val editedString = todo.editedAt.getDateTimeString()
                NoteItem(
                    note = todo,
                    editedString = editedString
                )
            }
        }
    }

    /* Card Operations */
    fun onDeleteTodo(homeScreenItem: HomeScreenItem) {
        setAction(SnackbarAction(true))
        viewModelScope.launch {
            when (homeScreenItem) {
                is ChecklistItem -> listRepo.deleteList(homeScreenItem.id)
                is NoteItem -> noteRepo.deleteNote(homeScreenItem.id)
            }
            lastDeletedCard = homeScreenItem
        }
    }

    fun undoDelete() {
        lastDeletedCard?.let { card ->
            viewModelScope.launch {
                when(card) {
                    is ChecklistItem -> {
                        listRepo.insertExistingList(card.checkList)
                        itemRepo.insertExisitingTodoItems(card.todoItems)
                    }
                    is NoteItem -> {
                        noteRepo.insertExistingNote(card.note)
                    }
                }
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

    fun onTodoFavourited(homeScreenItem: HomeScreenItem, isFavourited: Boolean) {
        viewModelScope.launch {
            when (homeScreenItem) {
                is ChecklistItem -> listRepo.updateIsFavourited(homeScreenItem.id, isFavourited)
                is NoteItem -> noteRepo.updateIsFavourited(homeScreenItem.id, isFavourited)
            }
        }
    }

    /* Navigation Functions */
    fun onHomeScreenItemClicked(homeScreenItem: HomeScreenItem) {
        when (homeScreenItem) {
            is ChecklistItem -> _navigateToListDetails.value = ListDetailNavArgs(homeScreenItem.id, homeScreenItem.checkList.listName)
            is NoteItem -> _navigateToNoteDetails.value = NoteDetailNavArgs(homeScreenItem.id, homeScreenItem.note.noteName)
        }
    }

    fun onFabButtonClicked() {
        _navigateToCreateTodo.value = true
    }

    fun onCreateListNavigated() {
        _navigateToCreateTodo.value = false
    }

    fun onTodoListNavigated() {
        _navigateToListDetails.value = null
    }

    fun onTodoNoteNavigated() {
        _navigateToNoteDetails.value = null
    }

    /* Action functions */
    fun onSnackbarDismissed() {
        setAction(SnackbarAction(false))
    }
}
