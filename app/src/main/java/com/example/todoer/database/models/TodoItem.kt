package com.example.todoer.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.joda.time.DateTime

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
    val listId: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: DateTime = DateTime(),

    @ColumnInfo(name = "item_name")
    val itemName: String,

    @ColumnInfo(name = "is_complete")
    val isComplete: Boolean = false
)
