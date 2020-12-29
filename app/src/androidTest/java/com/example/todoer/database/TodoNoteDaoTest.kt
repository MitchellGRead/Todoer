package com.example.todoer.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.testingmodule.coroutines.MainCoroutineRule
import com.example.testingmodule.mockfactories.TodoNoteMockFactory
import com.example.todoer.database.models.TodoNote
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TodoNoteDaoTest {

    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    private val noteFactory = TodoNoteMockFactory()

    private lateinit var database: TodoDatabase
    private lateinit var todoNoteDao: TodoNoteDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        todoNoteDao = database.todoNoteDao()
    }

    @After
    fun cleanUp() {
        database.close()
    }

    /* Inserting Tests */
    @Test
    fun `GIVEN_valid_todo_note_WHEN_insertTodoNote_THEN_note_is_inserted`() =
        mainCoroutineRule.runBlockingTest {
            val expected = noteFactory.todoNote1
            assertNull(getNote(expected.noteId))

            val noteId = todoNoteDao.insertTodoNote(expected)

            val actual = getNote(noteId)
            assertEquals(expected, actual)
        }

    /* Updating Tests */
    @Test
    fun `GIVEN_new_note_description_WHEN_updateNoteDescription_THEN_description_is_updated`() =
        mainCoroutineRule.runBlockingTest {
            val note = noteFactory.todoNote1
            val noteId = todoNoteDao.insertTodoNote(note)
            val expectedDescription = "what this is such     \n an odd \n\n string"
            assertEquals(note.noteDescription, getNote(noteId)?.noteDescription)
            assertNotSame(expectedDescription, note.noteDescription)

            todoNoteDao.updateNoteDescription(noteId, expectedDescription)

            val actual = getNote(noteId)
            assertNotSame(note.noteDescription, actual?.noteDescription)
            assertEquals(expectedDescription, actual?.noteDescription)
        }

    @Test
    fun `GIVEN_new_note_name_WHEN_updateNoteName_THEN_name_is_updated`() =
        mainCoroutineRule.runBlockingTest {
            val note = noteFactory.todoNote1
            val noteId = todoNoteDao.insertTodoNote(note)
            val expectedName = "what a new name"
            assertEquals(note.noteName, getNote(noteId)?.noteName)
            assertNotSame(note.noteName, expectedName)

            todoNoteDao.updateNoteName(noteId, expectedName)

            val actual = getNote(noteId)
            assertNotSame(note.noteName, actual?.noteName)
            assertEquals(expectedName, actual?.noteName)
        }

    /* Getting Tests */
    @Test
    fun `WHEN_observeTodoNotes_THEN_flow_with_current_notes_received`() =
        mainCoroutineRule.runBlockingTest {
            todoNoteDao.insertTodoNote(noteFactory.todoNote1)
            todoNoteDao.insertTodoNote(noteFactory.todoNote2)
            val expected: List<TodoNote> = noteFactory.todoNotes

            val actual = todoNoteDao.observeTodoNotes().take(1).toList()[0]

            assertEquals(expected.size, actual.size)
        }

    @Test
    fun `GIVEN_valid_note_id_WHEN_getDescriptionById_THEN_string_of_description_received`() =
        mainCoroutineRule.runBlockingTest {
            val note = noteFactory.todoNote1
            val noteId = todoNoteDao.insertTodoNote(note)
            val expectedDescription = note.noteDescription

            val actual = todoNoteDao.getNoteDescriptionById(noteId)

            assertEquals(expectedDescription, actual)
        }

    /* Deleting Tests */
    @Test
    fun `GIVEN_existing_id_WHEN_deleteNoteById_THEN_note_removed_from_database`() =
        mainCoroutineRule.runBlockingTest {
            val noteId = todoNoteDao.insertTodoNote(noteFactory.todoNote1)
            assertNotNull(getNote(noteId))

            todoNoteDao.deleteNoteById(noteId)

            assertNull(getNote(noteId))
        }

    /* Helpers */
    private suspend fun getNote(id: Long): TodoNote? {
        return todoNoteDao.getTodoNote(id)
    }
}
