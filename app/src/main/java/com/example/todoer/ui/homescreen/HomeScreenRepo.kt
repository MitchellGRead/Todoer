package com.example.todoer.ui.homescreen

import androidx.lifecycle.LiveData
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import kotlinx.coroutines.*
import javax.inject.Inject

class HomeScreenRepo @Inject constructor(
    private val todoListDao: TodoListDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    fun observeTodoLists(): LiveData<List<TodoList>> {
        return todoListDao.observeTodoLists()
    }

    suspend fun deleteList(listId: Long) {
        withContext(ioDispatcher) {
            todoListDao.deleteListById(listId)
        }
    }
}
