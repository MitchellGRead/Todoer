package com.example.todoer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoer.database.models.TodoItem
import com.example.todoer.database.models.TodoList

@Database(entities = [TodoList::class, TodoItem::class], version = 7, exportSchema = false)
@TypeConverters(value = [RoomTypeConverters::class])
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoListDao(): TodoListDao
    abstract fun todoItemDao(): TodoItemDao
}
