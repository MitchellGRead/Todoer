package com.example.todoer.domain

import com.example.todoer.daggerhilt.AppIoScope
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.models.TodoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoItemRepo @Inject constructor(
    private val todoItemDao: TodoItemDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @AppIoScope private val appIoScope: CoroutineScope
) {

    /* Inserting Operations */
    suspend fun insertTodoItem(listId: Long, itemName: String) {
        withContext(dispatcher) {
            todoItemDao.insertTodoItem(createTodoItem(listId, itemName))
        }
    }

    private fun createTodoItem(listId: Long, itemName: String) =
        TodoItem(listId = listId, itemName = itemName)

    suspend fun insertExisitingTodoItems(items: List<TodoItem>) {
        withContext(dispatcher) {
            todoItemDao.insertTodoItems(items)
        }
    }

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
    suspend fun getTodoItems(listId: Long): List<TodoItem> {
        return todoItemDao.getTodoItemsInList(listId) ?: emptyList()
    }

    fun observeTodoItems(listId: Long): Flow<List<TodoItem>> {
        return todoItemDao.observeTodoItemsInList(listId)
    }

    /* Deleting Operations */
    suspend fun deleteItem(item: TodoItem) {
        appIoScope.launch {
            todoItemDao.deleteItem(item)
        }.join()
    }

    suspend fun deleteItems(items: List<TodoItem>) {
        appIoScope.launch {
            todoItemDao.deleteItems(items)
        }.join()
    }
}
