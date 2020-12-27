package com.example.todoer.domain

import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoNoteDao
import com.example.todoer.database.models.TodoNote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TodoNoteRepo @Inject constructor(
    private val todoNoteDao: TodoNoteDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    /* Inserting Operations */
    suspend fun insertNote(noteName: String): Long {
        var noteId: Long
        withContext(dispatcher) {
            val todoNote = createTodoNote(noteName)
            noteId = todoNoteDao.insertTodoNote(todoNote)
        }
        return noteId
    }

    private fun createTodoNote(noteName: String): TodoNote {
        return TodoNote(noteName = noteName)
    }

    /* Updating Operations */
    suspend fun updateNoteName(noteId: Long, updatedName: String) {
        withContext(dispatcher) {
            todoNoteDao.updateNoteName(noteId, updatedName)
        }
    }

    suspend fun updateNoteDescription(noteId: Long, updatedDescription: String) {
        withContext(dispatcher) {
            todoNoteDao.updateNoteDescription(noteId, updatedDescription)
        }
    }

    /* Fetching Operations */
    fun observeTodoNotes(): Flow<List<TodoNote>> {
        return todoNoteDao.observeTodoNotes()
    }

    suspend fun getNoteDescription(noteId: Long): String {
        return todoNoteDao.getNoteDescriptionById(noteId).first()
    }

    suspend fun getTodoNotes(): List<TodoNote>? {
        return todoNoteDao.getTodoNotes()
    }

    /* Deleting Operations */
    suspend fun deleteNote(noteId: Long) {
        withContext(dispatcher) {
            todoNoteDao.deleteNoteById(noteId)
        }
    }
}
