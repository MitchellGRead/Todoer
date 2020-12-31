package com.example.todoer.domain

import com.example.todoer.daggerhilt.AppIoScope
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoNoteDao
import com.example.todoer.database.models.TodoNote
import com.example.todoer.ui.homescreen.recycler.NoteItem
import com.example.todoer.utils.Constants.USER_TYPE_DELAY
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class TodoNoteRepo @Inject constructor(
    private val todoNoteDao: TodoNoteDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @AppIoScope private val appIoScope: CoroutineScope
) {

    private var updateNoteJob: Job? = null

    init {
        Timber.d("Creating TodoNoteRepo")
    }

    /* Inserting Operations */
    suspend fun insertNote(noteName: String): Long {
        return withContext(dispatcher) {
            val todoNote = createTodoNote(noteName)
            todoNoteDao.insertTodoNote(todoNote)
        }
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
        updateNoteJob?.let { if (it.isActive) it.cancel() }
        updateNoteJob = appIoScope.launch {
            delay(USER_TYPE_DELAY)
            todoNoteDao.updateNoteDescription(noteId, updatedDescription)
        }
        updateNoteJob?.join()
    }

    /* Fetching Operations */
    fun observeNoteItems(): Flow<List<NoteItem>> {
        return todoNoteDao.observeTodoNotes().map { notes ->
            notes.map { NoteItem(it) }
        }.flowOn(dispatcher)
    }

    suspend fun getNoteDescription(noteId: Long): String? {
        return todoNoteDao.getNoteDescriptionById(noteId)
    }

    /* Deleting Operations */
    suspend fun deleteNote(noteId: Long) {
        withContext(dispatcher) {
            todoNoteDao.deleteNoteById(noteId)
        }
    }
}
