package com.example.todoer.ui.homescreen

import androidx.lifecycle.LiveData
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeScreenRepo @Inject constructor(
    private val todoListDao: TodoListDao
) {

    fun observeTodoLists(): LiveData<List<TodoList>> {
        return todoListDao.observeTodoLists()
    }

    suspend fun deleteList(listId: Long) {
        coroutineScope {
            launch { todoListDao.deleteListById(listId) }
        }
    }
}
