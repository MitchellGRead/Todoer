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
    suspend fun insertList(listName: String): Long {
        var listId: Long
        withContext(ioDispatcher) {
            val todoList = createTodoList(listName)
            listId = todoListDao.insertTodoList(todoList)
        }
        return listId
    }

    suspend fun insertList(todoList: TodoList) {
        withContext(ioDispatcher) {
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
        withContext(ioDispatcher) {
            todoListDao.deleteListById(listId)
        }
    }
}
