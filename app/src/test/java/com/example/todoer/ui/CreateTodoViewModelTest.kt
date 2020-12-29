package com.example.todoer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testingmodule.coroutines.MainCoroutineRule
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import com.example.todoer.navigation.NoteDetailNavArgs
import com.example.todoer.ui.createtodo.CreateTodoViewModel
import com.example.todoer.ui.createtodo.TodoType
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateTodoViewModelTest {

    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    private val listRepo: TodoListRepo = mock()
    private val noteRepo: TodoNoteRepo = mock()

    private val insertedListId = 1L
    private val insertedNoteId = 2L

    private lateinit var viewModel: CreateTodoViewModel

    @Before
    fun setup() {
        viewModel = CreateTodoViewModel(
            listRepo = listRepo,
            noteRepo = noteRepo
        )
    }

    init {
        mainCoroutineRule.runBlockingTest {
            whenever(listRepo.insertList(any())).thenReturn(insertedListId)
            whenever(noteRepo.insertNote(any())).thenReturn(insertedNoteId)
        }
    }

    @Test
    fun `GIVEN name and valid checklist string WHEN onCreateTodo THEN create and navigate to checklist`() =
        mainCoroutineRule.runBlockingTest {
            val name = "what"
            val typeId = "checklist"
            val expectedNavigation = ListDetailNavArgs(insertedListId, name)

            viewModel.onCreateTodo(name, typeId)

            verify(listRepo, times(1)).insertList(name)
            viewModel.navigateToTodoList
                .test()
                .assertHasValue()
                .assertValue(expectedNavigation)
                .assertHistorySize(1)
        }

    @Test
    fun `GIVEN name and valid note string WHEN onCreateTodo THEN create and navigate to note`() =
        mainCoroutineRule.runBlockingTest {
            val name = "what"
            val typeId = "note"
            val expectedNavigation = NoteDetailNavArgs(insertedNoteId, name)

            viewModel.onCreateTodo(name, typeId)

            verify(noteRepo, times(1)).insertNote(name)
            viewModel.navigateToTodoNote
                .test()
                .assertHasValue()
                .assertValue(expectedNavigation)
                .assertHistorySize(1)
        }

    @Test
    fun `GIVEN no name and valid checklist string WHEN onCreateTodo THEN create with default name and navigate to checklist`() =
        mainCoroutineRule.runBlockingTest {
            val name = ""
            val typeId = "checklist"
            val defaultName = TodoType.getDefaultName(TodoType.toTodoType(typeId))
            val expectedNavigation = ListDetailNavArgs(insertedListId, defaultName)

            viewModel.onCreateTodo(name, typeId)

            verify(listRepo, times(1)).insertList(defaultName)
            viewModel.navigateToTodoList
                .test()
                .assertHasValue()
                .assertValue(expectedNavigation)
                .assertHistorySize(1)
        }

    @Test
    fun `GIVEN name and valid note string WHEN onCreateTodo THEN create with default name and navigate to note`() =
        mainCoroutineRule.runBlockingTest {
            val name = ""
            val typeId = "note"
            val defaultName = TodoType.getDefaultName(TodoType.toTodoType(typeId))
            val expectedNavigation = NoteDetailNavArgs(insertedNoteId, defaultName)

            viewModel.onCreateTodo(name, typeId)

            verify(noteRepo, times(1)).insertNote(defaultName)
            viewModel.navigateToTodoNote
                .test()
                .assertHasValue()
                .assertValue(expectedNavigation)
                .assertHistorySize(1)
        }

    @Test(expected = IllegalArgumentException::class)
    fun `GIVEN an invalid todo type id WHEN onCreateTodo THEN IllegalArgumentException is thrown`() =
        mainCoroutineRule.runBlockingTest {
            val name = "error"
            val typeId = "invalidType"

            viewModel.onCreateTodo(name, typeId)
        }

}
