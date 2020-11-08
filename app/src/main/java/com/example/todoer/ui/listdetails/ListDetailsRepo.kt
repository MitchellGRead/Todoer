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

    /* TodoItem Operations */
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

    suspend fun updateItemCompleted(itemId: Long, isComplete: Boolean) {
        withContext(ioDispatcher) {
            todoItemDao.updateItemCompleted(itemId, isComplete)
        }
    }

    suspend fun getTodoItems(listId: Long): List<TodoItem>? {
        return todoItemDao.getTodoItemsInList(listId)
    }

    fun observeTodoItems(listId: Long): LiveData<List<TodoItem>> {
        return todoItemDao.observeTodoItemsInList(listId)
    }

    suspend fun deleteTodoItem(itemId: Long) {
        withContext(ioDispatcher) {
            todoItemDao.deleteItemById(itemId)
        }
    }

    /* TodoList Operations */
    suspend fun updateListCompleteTasks(listId: Long, completedTasks: Int) {
        withContext(ioDispatcher) {
            todoListDao.updateListCompletedTasks(listId, completedTasks)
        }
    }

    suspend fun updateListTotalTasks(listId: Long, totalTasks: Int) {
        withContext(ioDispatcher) {
            todoListDao.updateListTotalTasks(listId, totalTasks)
        }
    }
}
