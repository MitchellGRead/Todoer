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

    fun observeTodoLists(): LiveData<List<TodoList>> {
        return todoListDao.observeTodoLists()
    }

    suspend fun deleteList(listId: Long) {
        withContext(ioDispatcher) {
            todoListDao.deleteListById(listId)
        }
    }
}
