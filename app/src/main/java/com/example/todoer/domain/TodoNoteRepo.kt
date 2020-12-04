package com.example.todoer.domain

import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoNoteDao
import com.example.todoer.database.models.TodoList
import com.example.todoer.database.models.TodoNote
import kotlinx.coroutines.CoroutineDispatcher
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
}
