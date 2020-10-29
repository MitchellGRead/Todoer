package com.example.todoer.ui.listdetails.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.todoer.database.models.TodoItem

class ListDetailsDiffCallback : DiffUtil.ItemCallback<TodoItem>() {

    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}
