package com.example.todoer.domain

import com.example.todoer.daggerhilt.AppIoScope
import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import javax.inject.Inject

class TodoListRepo @Inject constructor(
    private val todoListDao: TodoListDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @AppIoScope private val appIoScope: CoroutineScope
) {

    /* Inserting Operations */
    suspend fun insertList(listName: String): Long {
        return withContext(dispatcher) {
            val todoList = createTodoList(listName)
            todoListDao.insertTodoList(todoList)
        }
    }

    private fun createTodoList(listName: String): TodoList {
        return TodoList(listName = listName)
    }

    suspend fun insertExistingList(todoList: TodoList): Long {
        return withContext(dispatcher) {
            todoListDao.insertTodoList(todoList)
        }
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
        withContext(dispatcher) {
            todoListDao.updateListName(listId, updatedName)
        }
    }
    
    fun updateEditDate(listId: Long, editedDate: DateTime) {
        appIoScope.launch {
            todoListDao.updateEditDate(listId, editedDate)
        }
    }

    suspend fun updateIsFavourited(listId: Long, isFavourited: Boolean) {
        withContext(dispatcher) {
            todoListDao.updatedIsFavourited(listId, isFavourited)
        }
    }

    /* Fetching Operations */
    fun observeTodoLists(): Flow<List<TodoList>> {
        return todoListDao.observeTodoLists()
    }

    /* Deleting Operations */
    suspend fun deleteList(listId: Long) {
        withContext(dispatcher) {
            todoListDao.deleteListById(listId)
        }
    }
}
