package com.example.testingmodule.mockfactories

import com.example.todoer.database.models.TodoItem
import com.example.todoer.database.models.TodoList
import org.joda.time.DateTime

class TodoItemMockFactory {

    private val itemName = "itemName"

    /* Update tasks with correct number of items below */
    val todoList: TodoList
        get() = TodoList(
            listId = 1L,
            completedTasks = 2,
            totalTasks = 3
        )

    val incompleteItem1: TodoItem
        get() = TodoItem(
            itemId = 10L,
            listId = todoList.listId,
            createdAt = DateTime(2020, 10, 4, 0, 0),
            itemName = itemName,
            isComplete = false
        )

    val completedItem1: TodoItem
        get() = TodoItem(
            itemId = 11L,
            listId = todoList.listId,
            createdAt = DateTime(2020, 10, 3, 0, 0),
            itemName = itemName,
            isComplete = true
        )

    val completedItem2: TodoItem
        get() = TodoItem(
            itemId = 12L,
            listId = todoList.listId,
            createdAt = DateTime(2020, 10, 5, 0, 0),
            itemName = itemName,
            isComplete = true
        )
    /* ----------------------- */

    /* Update tasks with correct number of rogue items below */
    val rogueList: TodoList
        get() = TodoList(
            listId = 2L,
            completedTasks = 0,
            totalTasks = 1
        )

    val rogueItem: TodoItem
        get() = TodoItem(
            itemId = 9L,
            listId = rogueList.listId,
            createdAt = DateTime(2020, 10, 5, 0, 0),
            itemName = itemName,
            isComplete = false
        )
    /* ----------------------- */

    val listTodoItems: List<TodoItem>
        get() = listOf(
            incompleteItem1,
            completedItem1
        )
}
