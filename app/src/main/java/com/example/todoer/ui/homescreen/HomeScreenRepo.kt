package com.example.todoer.ui.homescreen

import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import javax.inject.Inject

class HomeScreenRepo @Inject constructor(todoListDao: TodoListDao) {

    suspend fun fetchTodoLists(): List<TodoList> {
        return listOf(
            TodoList(
                listId = 1L,
                listName = "Groceries",
                listType = "",
                completedTasks = 59,
                totalTasks = 65
            ),
            TodoList(
                listId = 2L,
                listName = "Errands",
                listType = "",
                completedTasks = 3,
                totalTasks = 4
            )
        )
    }
}
