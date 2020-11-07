package com.example.todoer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoer.database.models.TodoItem
import com.example.todoer.database.models.TodoList

@Database(entities = [TodoList::class, TodoItem::class], version = 3, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoListDao(): TodoListDao
    abstract fun todoItemDao(): TodoItemDao
}
