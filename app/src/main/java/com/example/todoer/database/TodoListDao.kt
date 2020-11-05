package com.example.todoer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoer.database.models.TodoList

@Dao
interface TodoListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoList(todoList: TodoList)

    @Update
    suspend fun updateTodoList(todoList: TodoList)

    @Query(value = "SELECT * FROM todo_list_table")
    fun getAllTodoLists(): LiveData<List<TodoList>>

    @Query(value = "SELECT * FROM todo_list_table WHERE list_id = :listId_")
    suspend fun getTodoList(listId_: Long): TodoList?

    @Query(value = "DELETE FROM todo_list_table WHERE list_id = :listId_")
    suspend fun deleteListById(listId_: Long)
}
