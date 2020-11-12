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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    /* Inserting Operations */
    suspend fun insertTodoItem(listId: Long, itemName: String) {
        withContext(ioDispatcher) {
            todoItemDao.insertTodoItem(createTodoItem(listId, itemName))
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

    /* Updating Operations */
    suspend fun updateItemCompleted(itemId: Long, isComplete: Boolean) {
        withContext(ioDispatcher) {
            todoItemDao.updateItemCompleted(itemId, isComplete)
        }
    }

    /* Fetching Operations */
    suspend fun getTodoItems(listId: Long): List<TodoItem>? {
        return todoItemDao.getTodoItemsInList(listId)
    }

    fun observeTodoItems(listId: Long): LiveData<List<TodoItem>> {
        return todoItemDao.observeTodoItemsInList(listId)
    }

    /* Deleting Operations */
    suspend fun deleteTodoItem(itemId: Long) {
        withContext(ioDispatcher) {
            todoItemDao.deleteItemById(itemId)
        }
    }
}
