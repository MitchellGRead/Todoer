package com.example.todoer.ui.listdetails.recycler

data class TodoItemListeners(
    val onCheckboxSelected: (itemId: Long, isChecked: Boolean) -> Unit,
    val onDeleted: (itemId: Long) -> Unit,
    val onEdited: (itemId: Long, updatedText: String) -> Unit
)
