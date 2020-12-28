package com.example.todoer.mock

import com.example.todoer.database.models.TodoList
import com.example.todoer.database.models.TodoNote
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import com.example.todoer.ui.homescreen.recycler.NoteItem
import java.util.*

class TodoNoteMockFactory {

    private val noteName = "noteName"
    private val createdAt = Date(2020, 10, 10)
    private val noteType = TodoType.toListType(TodoType.NoteType)
    private val noteDescription = "I am a wee note description used for testing"

    val todoNote1: TodoNote
        get() = TodoNote(
            noteId = 50L,
            createdAt = createdAt,
            noteName = noteName,
            todoType = noteType,
            noteDescription = noteDescription
        )

    val todoNote2: TodoNote
        get() = TodoNote(
            noteId = 51L,
            createdAt = createdAt,
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
            return NoteItem(this)
        }
    }
}
