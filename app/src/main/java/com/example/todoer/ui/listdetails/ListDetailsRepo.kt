package com.example.todoer.ui.listdetails

import androidx.lifecycle.LiveData
import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.models.TodoItem
import javax.inject.Inject

class ListDetailsRepo @Inject constructor(private val todoItemDao: TodoItemDao) {

    fun fetchTodoItems(): LiveData<List<TodoItem>> {
        return todoItemDao.getAllTodoItems()
    }

    suspend fun insertTodoItem(listId: Long, itemName: String) {
        todoItemDao.insertTodoItem(createTodoItem(listId, itemName))
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
