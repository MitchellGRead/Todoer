package com.example.testingmodule.mockfactories

import com.example.testingmodule.mockfactories.TodoListMockFactory.Companion.toHomeScreenItem
import com.example.todoer.database.models.TodoNote
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.homescreen.recycler.NoteItem
import com.example.todoer.utils.DateTimeUtils.getDateTimeString
import org.joda.time.DateTime
import java.util.*

class TodoNoteMockFactory {

    private val noteName = "noteName"
    private val createdAt = DateTime(2020, 10, 10, 0, 0)
    private val noteType = TodoType.toTodoType(TodoType.NoteTypeId)
    private val noteDescription = "I am a wee note description used for testing"

    val todoNote1: TodoNote
        get() = TodoNote(
            noteId = 50L,
            createdAt = createdAt,
            editedAt = DateTime(2020, 10, 10, 23, 0),
            noteName = noteName,
            todoType = noteType,
            noteDescription = noteDescription
        )

    val todoNote2: TodoNote
        get() = TodoNote(
            noteId = 51L,
            createdAt = createdAt,
            editedAt = DateTime(2020, 10, 10, 21, 0),
            noteName = noteName,
            todoType = noteType,
            noteDescription = ""
        )

    val todoNotes: List<TodoNote>
        get() = listOf(
            todoNote1,
            todoNote2
        )

    val noteItems: List<NoteItem>
        get() = todoNotes.map { it.toHomeScreenItem() }

    companion object {
        fun TodoNote.toHomeScreenItem(): NoteItem {
            val editedString = this.editedAt.getDateTimeString()
            return NoteItem(this, editedString)
        }
    }
}
