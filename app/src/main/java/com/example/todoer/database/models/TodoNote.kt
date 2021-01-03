package com.example.todoer.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoer.ui.createtodo.TodoType
import org.joda.time.DateTime

@Entity(tableName = "todo_note_table")
data class TodoNote(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val noteId: Long = 0L,

    @ColumnInfo(name = "created_at")
    val createdAt: DateTime = DateTime(),

    @ColumnInfo(name = "edited_at")
    val editedAt: DateTime = DateTime(),

    @ColumnInfo(name = "favourited")
    val isFavourited: Boolean = false,

    @ColumnInfo(name = "note_name")
    val noteName: String = "",

    @ColumnInfo(name = "note_type")
    val todoType: TodoType = TodoType.toTodoType(TodoType.NoteTypeId),

    @ColumnInfo(name = "note_description")
    val noteDescription: String = ""
)
