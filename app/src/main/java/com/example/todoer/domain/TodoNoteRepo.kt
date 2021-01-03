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
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

class TodoNoteRepo @Inject constructor(
    private val todoNoteDao: TodoNoteDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @AppIoScope private val appIoScope: CoroutineScope
) {

    private var updateDescriptionJob: Job? = null
    private var updateNameJob: Job? = null

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
        updateNameJob?.cancel()
        updateNameJob = appIoScope.launch {
            delay(USER_TYPE_DELAY)
            todoNoteDao.updateNoteName(noteId, updatedName)
        }
    }

    suspend fun updateNoteDescription(noteId: Long, updatedDescription: String) {
        updateDescriptionJob?.cancel()
        updateDescriptionJob = appIoScope.launch {
            delay(USER_TYPE_DELAY)
            todoNoteDao.updateNoteDescription(noteId, updatedDescription)
        }
    }

    fun updateEditDate(noteId: Long, editedDate: DateTime) {
        appIoScope.launch {
            todoNoteDao.updateEditDate(noteId, editedDate)
        }
    }

    suspend fun updateIsFavourited(noteId: Long, isFavourited: Boolean) {
        withContext(dispatcher) {
            todoNoteDao.updatedIsFavourited(noteId, isFavourited)
        }
    }

    /* Fetching Operations */
    fun observeTodoNotes(): Flow<List<TodoNote>> {
        return todoNoteDao.observeTodoNotes()
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
