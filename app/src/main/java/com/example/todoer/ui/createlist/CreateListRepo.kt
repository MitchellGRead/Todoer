package com.example.todoer.ui.createlist

import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import javax.inject.Inject

class CreateListRepo @Inject constructor(private val todoListDao: TodoListDao) {

    suspend fun insertList(listName: String) {
        val todoList = createTodoList(listName)
        todoListDao.insertTodoList(todoList)
    }

    private fun createTodoList(
        listName: String,
        listType: String = ""
    ): TodoList {
        return TodoList(
            listName = listName,
            listType = listType,
            completedTasks = 0,
            totalTasks = 0
        )
    }
}
