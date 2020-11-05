package com.example.todoer.ui.listdetails

import androidx.lifecycle.LiveData
import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoItem
import com.example.todoer.database.models.TodoList
import javax.inject.Inject

class ListDetailsRepo @Inject constructor(
    private val todoListDao: TodoListDao,
    private val todoItemDao: TodoItemDao
) {

    suspend fun fetchTodoList(listId: Long): TodoList? {
        return todoListDao.getTodoList(listId)
    }

    suspend fun updateTodoList(todoList: TodoList) {
        todoListDao.updateTodoList(todoList)
    }

    fun fetchTodoItems(listId: Long): LiveData<List<TodoItem>> {
        return todoItemDao.getAllTodoItemsForList(listId)
    }

    suspend fun insertTodoItem(todoList: TodoList, itemName: String) {
        todoItemDao.insertTodoItem(createTodoItem(todoList.listId, itemName))
    }

    private fun createTodoItem(listId: Long, itemName: String) =
        TodoItem(
            listId = listId,
            itemName = itemName,
            itemDescription = "",
            isComplete = false,
            iconUrl = ""
        )
}
