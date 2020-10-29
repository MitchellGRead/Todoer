package com.example.todoer.ui.homescreen.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.todoer.database.models.TodoList

class HomeScreenDiffCallback : DiffUtil.ItemCallback<TodoList>() {

    override fun areItemsTheSame(oldItem: TodoList, newItem: TodoList): Boolean {
        return oldItem.listId == newItem.listId
    }

    override fun areContentsTheSame(oldItem: TodoList, newItem: TodoList): Boolean {
        return oldItem == newItem
    }
}
