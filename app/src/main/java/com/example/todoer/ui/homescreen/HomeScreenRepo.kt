package com.example.todoer.ui.homescreen

import androidx.lifecycle.LiveData
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import javax.inject.Inject

class HomeScreenRepo @Inject constructor(private val todoListDao: TodoListDao) {

    fun fetchTodoLists(): LiveData<List<TodoList>> {
        return todoListDao.getAllTodoLists()
    }

    suspend fun deleteList(listId: Long) {
        todoListDao.deleteListById(listId)
    }
}
