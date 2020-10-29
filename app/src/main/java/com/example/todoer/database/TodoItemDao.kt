package com.example.todoer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoer.database.models.TodoItem

@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(item: TodoItem)

    @Query(value = "SELECT * FROM todo_item_table WHERE itemId = :itemId_ ")
    fun getTodoItem(itemId_: Long): LiveData<TodoItem>

    @Query(value = "SELECT * FROM todo_item_table")
    fun getAllTodoItems(): LiveData<List<TodoItem>>
}
