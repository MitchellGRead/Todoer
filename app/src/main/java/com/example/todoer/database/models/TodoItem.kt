package com.example.todoer.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "todo_item_table",
    foreignKeys = [ForeignKey(
        entity = TodoList::class,
        parentColumns = ["list_id"],
        childColumns = ["list_id"],
        onDelete = ForeignKey.CASCADE)]
)
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val itemId: Long = 0L,

    @ColumnInfo(name = "list_id", index = true)
    val listId: Long = 0L,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "item_name")
    val itemName: String = "",

    @ColumnInfo(name = "item_description")
    val itemDescription: String = "",

    @ColumnInfo(name = "is_complete")
    val isComplete: Boolean = false,

    @ColumnInfo(name = "icon_url")
    val iconUrl: String = ""
)
