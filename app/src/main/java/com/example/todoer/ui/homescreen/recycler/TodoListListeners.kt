package com.example.todoer.ui.homescreen.recycler

data class TodoListListener(
    val onClick: (listId: Long) -> Unit
)

data class TodoListMenuOptionListeners(
    val renameClickListener: (listId: Long) -> Unit,
    val deleteClickListener: (listId: Long) -> Unit,
    val shareClickListener: (listId: Long) -> Unit
)
