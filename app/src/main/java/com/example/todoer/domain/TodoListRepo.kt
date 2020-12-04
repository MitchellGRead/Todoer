package com.example.todoer.domain

import androidx.lifecycle.LiveData
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoListRepo @Inject constructor(
    private val todoListDao: TodoListDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    /* Inserting Operations */
    suspend fun insertList(listName: String): Long {
        var listId: Long
        withContext(dispatcher) {
            val todoList = createTodoList(listName)
            listId = todoListDao.insertTodoList(todoList)
        }
        return listId
    }

    private fun createTodoList(listName: String): TodoList {
        return TodoList(listName = listName)
    }

    /* Updating Operations */
    suspend fun updateListCompleteTasks(listId: Long, completedTasks: Int) {
        withContext(dispatcher) {
            todoListDao.updateListCompletedTasks(listId, completedTasks)
        }
    }

    suspend fun updateListTotalTasks(listId: Long, totalTasks: Int) {
        withContext(dispatcher) {
            todoListDao.updateListTotalTasks(listId, totalTasks)
        }
    }

    suspend fun updateListName(listId: Long, updatedName: String) {
        todoListDao.updateListName(listId, updatedName)
    }

    /* Fetching Operations */
    fun observeTodoLists(): LiveData<List<TodoList>> {
        return todoListDao.observeTodoLists()
    }

    suspend fun getTodoList(listId: Long): TodoList? {
        return todoListDao.getTodoList(listId)
    }

    /* Deleting Operations */
    suspend fun deleteList(listId: Long) {
        withContext(dispatcher) {
            todoListDao.deleteListById(listId)
        }
    }
}
