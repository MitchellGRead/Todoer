package com.example.todoer.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoer.ui.createtodo.TodoType
import org.joda.time.DateTime

@Entity(tableName = "todo_list_table")
data class TodoList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    val listId: Long = 0L,

    @ColumnInfo(name = "created_at")
    val createdAt: DateTime = DateTime(),

    @ColumnInfo(name = "edited_at")
    val editedAt: DateTime = DateTime(),

    @ColumnInfo(name = "favourited")
    val isFavourited: Boolean = false,

    @ColumnInfo(name = "list_name")
    val listName: String = "",

    @ColumnInfo(name = "list_type")
    val todoType: TodoType = TodoType.toTodoType(TodoType.CheckListTypeId),

    @ColumnInfo(name = "completed_tasks")
    val completedTasks: Int = 0,

    @ColumnInfo(name = "total_tasks")
    val totalTasks: Int = 0
)
