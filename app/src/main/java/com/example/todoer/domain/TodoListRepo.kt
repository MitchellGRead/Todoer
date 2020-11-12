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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    /* Inserting Operations */
    suspend fun insertList(listName: String) {
        withContext(ioDispatcher) {
            val todoList = createTodoList(listName)
            todoListDao.insertTodoList(todoList)
        }
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

    /* Updating Operations */
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

    /* Fetching Operations */
    fun observeTodoLists(): LiveData<List<TodoList>> {
        return todoListDao.observeTodoLists()
    }

    /* Deleting Operations */
    suspend fun deleteList(listId: Long) {
        withContext(ioDispatcher) {
            todoListDao.deleteListById(listId)
        }
    }
}
