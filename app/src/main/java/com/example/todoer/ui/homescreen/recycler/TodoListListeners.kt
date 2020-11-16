package com.example.todoer.ui.homescreen.recycler

data class TodoListListeners(
    val onClickList: (listId: Long, listName: String) -> Unit,
    val renameClickListener: (listId: Long, updatedName: String) -> Unit,
    val deleteClickListener: (listId: Long) -> Unit,
    val shareClickListener: (listId: Long) -> Unit
)
