package com.example.testingmodule.mockfactories

import com.example.todoer.database.models.TodoItem
import java.util.*

class TodoItemMockFactory {

    private val listFactory = TodoListMockFactory()
    private val itemName = "itemName"

    val todoList = listFactory.todoList1
    val rogueList = listFactory.todoList2

    val incompleteItem: TodoItem
        get() = TodoItem(
            itemId = 10L,
            listId = todoList.listId,
            createdAt = Date(2020, 10, 4),
            itemName = itemName,
            isComplete = false
        )

    val completedItem: TodoItem
        get() = TodoItem(
            itemId = 11L,
            listId = todoList.listId,
            createdAt = Date(2020, 10, 5),
            itemName = itemName,
            isComplete = true
        )

    val rogueItem: TodoItem
        get() = TodoItem(
            itemId = 9L,
            listId = rogueList.listId,
            createdAt = Date(2020, 10, 4),
            itemName = itemName,
            isComplete = false
        )

    val listTodoItems: List<TodoItem>
        get() = listOf(
            incompleteItem,
            completedItem
        )
}
