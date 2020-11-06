package com.example.todoer.ui.listdetails.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.database.models.TodoItem

class ListDetailsAdapter(
    private val todoItemListeners: TodoItemListeners
) : ListAdapter<TodoItem, RecyclerView.ViewHolder>(ListDetailsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodoItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoItemViewHolder -> {
                val todoItem = getItem(position)
                holder.bind(todoItem, todoItemListeners)
            }
        }
    }
}
