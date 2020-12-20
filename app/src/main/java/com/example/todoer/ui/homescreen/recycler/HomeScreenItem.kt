package com.example.todoer.ui.homescreen.recycler

import com.example.todoer.database.models.TodoList
import com.example.todoer.database.models.TodoNote

sealed class HomeScreenItem {
    abstract val id: Long

    companion object {
        const val ITEM_VIEW_TYPE_CHECKLIST = 1
        const val ITEM_VIEW_TYPE_NOTE = 2
    }
}

data class ChecklistItem(val checkList: TodoList) : HomeScreenItem() {
    override val id = checkList.listId
}
data class NoteItem(val note: TodoNote) : HomeScreenItem() {
    override val id = note.noteId
}
