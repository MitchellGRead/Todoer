package com.example.todoer.ui.homescreen.recycler

import com.example.todoer.ui.createtodo.TodoType

data class TodoCardListeners(
    val onClickTodoCard: (todoId: Long, cardType: TodoType, todoName: String) -> Unit,
    val renameTodoListener: (todoId: Long, cardType: TodoType, updatedName: String) -> Unit,
    val deleteTodoListener: (todoId: Long, cardType: TodoType) -> Unit
)
