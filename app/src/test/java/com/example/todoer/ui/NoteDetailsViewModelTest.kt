package com.example.todoer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testingmodule.coroutines.MainCoroutineRule
import com.example.testingmodule.mockfactories.TodoListMockFactory
import com.example.testingmodule.mockfactories.TodoNoteMockFactory
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.ui.homescreen.HomeScreenViewModel
import com.example.todoer.ui.notedetails.NoteDetailsViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NoteDetailsViewModelTest {

    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    private val noteRepo: TodoNoteRepo = mock()

    private val noteFactory = TodoNoteMockFactory()
    private val note = noteFactory.todoNote1

    private lateinit var viewModel: NoteDetailsViewModel

    @Before
    fun setup() {
        viewModel = NoteDetailsViewModel(
            noteId = note.noteId,
            noteRepo = noteRepo
        )
    }

    init {
        mainCoroutineRule.runBlockingTest {
            whenever(noteRepo.getNoteDescription(note.noteId)).thenReturn(note.noteDescription)
        }
    }

    @Test
    fun `WHEN getNoteDescription THEN description returned`() =
        mainCoroutineRule.runBlockingTest {
            val expected = note.noteDescription

            val actual = viewModel.getNoteDescription()

            assertEquals(expected, actual)
        }

    @Test
    fun `GIVEN new description WHEN saveNoteDescription THEN description saved`() =
        mainCoroutineRule.runBlockingTest {
            val newDescription = "a wee test"

            viewModel.saveNoteDescription(newDescription)

            verify(noteRepo, times(1)).updateNoteDescription(note.noteId, newDescription)
        }

}
