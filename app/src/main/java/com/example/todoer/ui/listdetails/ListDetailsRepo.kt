package com.example.todoer.ui.listdetails

import androidx.lifecycle.LiveData
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoItem
import com.example.todoer.database.models.TodoList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListDetailsRepo @Inject constructor(
    private val todoListDao: TodoListDao,
    private val todoItemDao: TodoItemDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun insertTodoItem(todoList: TodoList, itemName: String) {
        withContext(ioDispatcher) {
            todoItemDao.insertTodoItem(createTodoItem(todoList.listId, itemName))
        }
    }

    private fun createTodoItem(listId: Long, itemName: String) =
        TodoItem(
            listId = listId,
            itemName = itemName,
            itemDescription = "",
            isComplete = false,
            iconUrl = ""
        )

    suspend fun updateTodoList(todoList: TodoList) {
        withContext(ioDispatcher) {
            todoListDao.updateTodoList(todoList)
        }
    }

    suspend fun updateListTotalTasks(listId: Long, totalTasks: Int) {
        withContext(ioDispatcher) {
            todoListDao.updateListTotalTasks(listId, totalTasks)
        }
    }

    suspend fun getTodoList(listId: Long): TodoList? {
        return todoListDao.getTodoList(listId)
    }

    suspend fun getTodoItems(listId: Long): List<TodoItem>? {
        return todoItemDao.getTodoItemsInList(listId)
    }

    fun observeTodoItems(listId: Long): LiveData<List<TodoItem>> {
        return todoItemDao.observeTodoItemsInList(listId)
    }
}
