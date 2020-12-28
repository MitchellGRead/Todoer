package com.example.todoer.mock

import com.example.todoer.database.models.TodoList
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import java.util.*

object TodoListMockFactory {

    private val listName = "listName"
    private val createdAt = Date(2020, 10, 10)
    private val listType = TodoType.toListType(TodoType.CheckListType)
    private val completedTasks = 2
    private val totalTasks = 4

    val todoList1: TodoList
        get() = TodoList(
            listId = 1L,
            listName = listName,
            todoType = listType,
            createdAt = createdAt,
            completedTasks = completedTasks,
            totalTasks = totalTasks
        )

    val todoList2: TodoList
        get() = TodoList(
            listId = 2L,
            listName = listName,
            todoType = listType,
            createdAt = createdAt,
            completedTasks = completedTasks,
            totalTasks = totalTasks
        )

    val todoLists: List<TodoList>
        get() = listOf(
            todoList1,
            todoList2
        )

    val checkListItems: List<ChecklistItem>
        get() = todoLists.map { ChecklistItem(it) }
}
