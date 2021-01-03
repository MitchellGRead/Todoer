package com.example.todoer.ui.homescreen.recycler

import com.example.todoer.database.models.TodoList
import com.example.todoer.database.models.TodoNote
import org.joda.time.DateTime

sealed class HomeScreenItem {
    abstract val id: Long
    abstract val createdDate: DateTime
    abstract val editedDate: DateTime

    companion object {
        const val ITEM_VIEW_TYPE_CHECKLIST = 1
        const val ITEM_VIEW_TYPE_NOTE = 2
    }
}

data class ChecklistItem(val checkList: TodoList) : HomeScreenItem() {
    override val id: Long
        get() = checkList.listId
    override val createdDate: DateTime
        get() = checkList.createdAt
    override val editedDate: DateTime
        get() = checkList.editedAt
}
data class NoteItem(val note: TodoNote) : HomeScreenItem() {
    override val id = note.noteId
    override val createdDate: DateTime
        get() = note.createdAt
    override val editedDate: DateTime
        get() = note.editedAt
}
