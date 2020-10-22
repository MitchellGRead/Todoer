package com.example.todoer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoer.database.models.TodoList

@Dao
interface TodoListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoList(todoList: TodoList)

    @Query(value = "SELECT * FROM todo_list_table")
    fun getAllTodoLists(): LiveData<List<TodoList>>
}
