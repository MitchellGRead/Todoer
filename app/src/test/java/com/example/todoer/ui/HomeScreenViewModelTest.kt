package com.example.todoer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testingmodule.coroutines.MainCoroutineRule
import com.example.testingmodule.mockfactories.TodoListMockFactory
import com.example.testingmodule.mockfactories.TodoListMockFactory.Companion.toHomeScreenItem
import com.example.testingmodule.mockfactories.TodoNoteMockFactory
import com.example.testingmodule.mockfactories.TodoNoteMockFactory.Companion.toHomeScreenItem
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.ui.homescreen.HomeScreenViewModel
import com.example.todoer.ui.homescreen.recycler.HomeScreenItem
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeScreenViewModelTest {

    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    private val listRepo: TodoListRepo = mock()
    private val noteRepo: TodoNoteRepo = mock()

    private val listFactory = TodoListMockFactory()
    private val noteFactory = TodoNoteMockFactory()
    private val homeScreenItems = listFactory.checkListItems + noteFactory.noteItems

    private lateinit var viewModel: HomeScreenViewModel

    @Before
    fun setup() {
        viewModel = HomeScreenViewModel(
            listRepo = listRepo,
            noteRepo = noteRepo,
            dispatcher = Dispatchers.Unconfined
        )
    }

    init {
        whenever(listRepo.observeTodoLists()).thenReturn(flow { emit(listFactory.todoLists) })
        whenever(noteRepo.observeTodoNotes()).thenReturn(flow { emit(noteFactory.todoNotes) })
    }

    @Test
    fun `GIVEN no sorting WHEN viewmodel sortContent THEN home screen items fetched in MRU order`() {
        val expected = homeScreenItems
            .sortedByDescending { it.editedDate }

        viewModel.sortContent(false)

        viewModel.homeScreenItems
            .test()
            .assertHasValue()
            .assertValue(expected)
            .assertHistorySize(1)
    }

    @Test
    fun `GIVEN sortOption true WHEN viewmodel sortContent THEN home screen items fetched with MRU and favs at top`() {
        val expected = homeScreenItems
            .sortedWith(compareByDescending<HomeScreenItem> { it.isFavourited }.thenByDescending{ it.editedDate })

        viewModel.sortContent(true)

        viewModel.homeScreenItems
            .test()
            .assertHasValue()
            .assertValue(expected)
            .assertHistorySize(1)
    }

    @Test
    fun `GIVEN multiple calls to sortContent WHEN viewmodel sortContent THEN home screen items fetched with most recent option`() {
        val expected = homeScreenItems
            .sortedByDescending { it.editedDate }

        viewModel.sortContent(false)
        viewModel.sortContent(true)
        viewModel.sortContent(false)

        viewModel.homeScreenItems
            .test()
            .assertHasValue()
            .assertValue(expected)
            .assertHistorySize(1)
    }

    @Test
    fun `GIVEN checklist todo type WHEN onDeleteTodo THEN verify delete list repo called`() =
        mainCoroutineRule.runBlockingTest {
            val checkList = listFactory.todoList1.toHomeScreenItem()

            viewModel.onDeleteTodo(checkList)

            verify(listRepo).deleteList(checkList.id)
    }

    @Test
    fun `GIVEN note todo type WHEN onDeleteTodo THEN verify delete note repo called`() =
        mainCoroutineRule.runBlockingTest {
            val note = noteFactory.todoNote1.toHomeScreenItem()

            viewModel.onDeleteTodo(note)

            verify(noteRepo).deleteNote(note.id)
    }

    @Test
    fun `GIVEN updated name for checklist WHEN onRenameTodo THEN verify checklist renamed`() =
        mainCoroutineRule.runBlockingTest {
            val checkList = listFactory.todoList1.toHomeScreenItem()
            val newName = "new name"

            viewModel.onRenameTodo(checkList, newName)

            verify(listRepo).updateListName(checkList.id, newName)
        }

    @Test
    fun `GIVEN updated name for note WHEN onRenameTodo THEN verify note renamed`() =
        mainCoroutineRule.runBlockingTest {
            val note = noteFactory.todoNote1.toHomeScreenItem()
            val newName = "new name"

            viewModel.onRenameTodo(note, newName)

            verify(noteRepo).updateNoteName(note.id, newName)
        }
}
