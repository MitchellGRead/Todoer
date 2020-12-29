package com.example.todoer.domain

import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.models.TodoList
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
        withContext(dispatcher) {
            todoListDao.updateListName(listId, updatedName)
        }
    }

    /* Fetching Operations */
    fun observeChecklistItems(): Flow<List<ChecklistItem>> {
        return todoListDao.observeTodoLists().map { checklists ->
            checklists.map { ChecklistItem(it) }
        }.flowOn(dispatcher)
    }

    /* Deleting Operations */
    suspend fun deleteList(listId: Long) {
        withContext(dispatcher) {
            todoListDao.deleteListById(listId)
        }
    }
}
