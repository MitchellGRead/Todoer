package com.example.todoer.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_item_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0L,

    @ColumnInfo(name = "list_id")
    val listId: Long = 0L,

    @ColumnInfo(name = "item_name")
    val itemName: String = "",

    @ColumnInfo(name = "item_description")
    val itemDescription: String = "",

    @ColumnInfo(name = "is_complete")
    val isComplete: Boolean = false,

    @ColumnInfo(name = "icon_url")
    val iconUrl: String = ""
)
