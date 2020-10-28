package com.example.todoer.ui.homescreen.recycler

data class TodoListMenuOptionListeners(
    val renameClickListener: (listId: Long) -> Unit,
    val deleteClickListener: (listId: Long) -> Unit,
    val shareClickListener: (listId: Long) -> Unit
)
