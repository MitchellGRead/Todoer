package com.example.todoer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoer.MainCoroutineRule
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.mock.TodoListMockFactory
import com.example.todoer.mock.TodoListMockFactory.Companion.toHomeScreenItem
import com.example.todoer.mock.TodoNoteMockFactory
import com.example.todoer.mock.TodoNoteMockFactory.Companion.toHomeScreenItem
import com.example.todoer.ui.homescreen.HomeScreenViewModel
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        whenever(listRepo.observeChecklistItems()).thenReturn(flow { emit(listFactory.checkListItems) })
        whenever(noteRepo.observeNoteItems()).thenReturn(flow { emit(noteFactory.noteItems) })
    }

    @Test
    fun `WHEN viewmodel is created THEN home screen items fetched`() {
        val expected = listFactory.checkListItems + noteFactory.noteItems

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
