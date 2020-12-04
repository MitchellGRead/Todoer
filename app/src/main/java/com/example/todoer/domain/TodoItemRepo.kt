package com.example.todoer.domain

import androidx.lifecycle.LiveData
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.models.TodoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoItemRepo @Inject constructor(
    private val todoItemDao: TodoItemDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    /* Inserting Operations */
    suspend fun insertTodoItem(listId: Long, itemName: String) {
        withContext(dispatcher) {
            todoItemDao.insertTodoItem(createTodoItem(listId, itemName))
        }
    }

    suspend fun insertTodoItem(todoItem: TodoItem) {
        withContext(dispatcher) {
            todoItemDao.insertTodoItem(todoItem)
        }
    }

    private fun createTodoItem(listId: Long, itemName: String) =
        TodoItem(listId = listId, itemName = itemName)

    /* Updating Operations */
    suspend fun updateItemCompleted(itemId: Long, isComplete: Boolean) {
        withContext(dispatcher) {
            todoItemDao.updateItemCompleted(itemId, isComplete)
        }
    }

    suspend fun updateItemName(itemId: Long, updatedName: String) {
        withContext(dispatcher) {
            todoItemDao.updateItemName(itemId, updatedName)
        }
    }

    /* Fetching Operations */
    suspend fun getTodoItem(itemId: Long): TodoItem? {
        return todoItemDao.getTodoItem(itemId)
    }

    suspend fun getTodoItems(listId: Long): List<TodoItem>? {
        return todoItemDao.getTodoItemsInList(listId)
    }

    fun observeTodoItems(listId: Long): LiveData<List<TodoItem>> {
        return todoItemDao.observeTodoItemsInList(listId)
    }

    /* Deleting Operations */
    suspend fun deleteTodoItem(itemId: Long) {
        withContext(dispatcher) {
            todoItemDao.deleteItemById(itemId)
        }
    }
}
