package com.example.todoer.mock

import com.example.todoer.database.models.TodoItem

object TodoItemMockFactory {

    val todoList = TodoListMockFactory.todoList1
    private val itemName = "itemName"

    val incompleteItem: TodoItem
        get() = TodoItem(
            itemId = 10L,
            listId = todoList.listId ,
            itemName = itemName,
            isComplete = false
        )

    val completedItem: TodoItem
        get() = TodoItem(
            itemId = 10L,
            listId = todoList.listId,
            itemName = itemName,
            isComplete = true
        )
}
