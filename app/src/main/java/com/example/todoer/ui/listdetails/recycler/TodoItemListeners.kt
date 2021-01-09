package com.example.todoer.ui.listdetails.recycler

import com.example.todoer.database.models.TodoItem

data class TodoItemListeners(
    val onCheckboxSelected: (itemId: Long, isChecked: Boolean) -> Unit,
    val onDeleted: (item: TodoItem) -> Unit,
    val onEdited: (itemId: Long, updatedText: String) -> Unit
)
