package com.example.todoer.ui.homescreen.recycler

data class TodoListListeners(
    val onClickList: (listId: Long) -> Unit,
    val renameClickListener: (listId: Long) -> Unit,
    val deleteClickListener: (listId: Long) -> Unit,
    val shareClickListener: (listId: Long) -> Unit
)
