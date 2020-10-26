package com.example.todoer.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list_table")
data class TodoList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    val listId: Long = 0L,

    @ColumnInfo(name = "list_name")
    val listName: String = "",

    @ColumnInfo(name = "list_type")
    val listType: String = "",

    @ColumnInfo(name = "completed_tasks")
    val completedTasks: Int = 0,

    @ColumnInfo(name = "total_tasks")
    val totalTasks: Int = 0
)
