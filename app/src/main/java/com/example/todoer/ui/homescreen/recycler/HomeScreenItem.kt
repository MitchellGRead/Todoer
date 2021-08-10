package com.example.todoer.ui.homescreen.recycler

import com.example.todoer.database.models.TodoItem
import com.example.todoer.database.models.TodoList
import com.example.todoer.database.models.TodoNote
import org.joda.time.DateTime

sealed class HomeScreenItem {
    abstract val id: Long
    abstract val createdDate: DateTime
    abstract val editedDate: DateTime
    abstract val editedDateString: String
    abstract val isFavourited: Boolean

    companion object {
        const val ITEM_VIEW_TYPE_CHECKLIST = 1
        const val ITEM_VIEW_TYPE_NOTE = 2
    }
}

data class ChecklistItem(
    val checkList: TodoList,
    val todoItems: List<TodoItem>,  // Used to keep a copy of when we delete a list
    val editedString: String
) : HomeScreenItem() {
    override val id: Long
        get() = checkList.listId
    override val createdDate: DateTime
        get() = checkList.createdAt
    override val editedDate: DateTime
        get() = checkList.editedAt
    override val editedDateString: String
        get() = editedString
    override val isFavourited: Boolean
        get() = checkList.isFavourited
}

data class NoteItem(
    val note: TodoNote,
    val editedString: String
) : HomeScreenItem() {
    override val id = note.noteId
    override val createdDate: DateTime
        get() = note.createdAt
    override val editedDate: DateTime
        get() = note.editedAt
    override val editedDateString: String
        get() = editedString
    override val isFavourited: Boolean
        get() = note.isFavourited
}
