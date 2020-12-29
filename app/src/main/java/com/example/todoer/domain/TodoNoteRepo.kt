package com.example.todoer.domain

import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoNoteDao
import com.example.todoer.database.models.TodoNote
import com.example.todoer.ui.homescreen.recycler.NoteItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
    fun observeNoteItems(): Flow<List<NoteItem>> {
        return todoNoteDao.observeTodoNotes().map { notes ->
            notes.map { NoteItem(it) }
        }.flowOn(dispatcher)
    }

    suspend fun getNoteDescription(noteId: Long): String {
        return todoNoteDao.getNoteDescriptionById(noteId) ?: ""
    }

    /* Deleting Operations */
    suspend fun deleteNote(noteId: Long) {
        withContext(dispatcher) {
            todoNoteDao.deleteNoteById(noteId)
        }
    }
}
