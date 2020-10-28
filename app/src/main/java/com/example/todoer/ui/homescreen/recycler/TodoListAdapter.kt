package com.example.todoer.ui.homescreen.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.database.models.TodoList

class TodoListAdapter(
    private val context: Context?,
    private val menuOptionListeners: TodoListMenuOptionListeners
) : ListAdapter<TodoList, RecyclerView.ViewHolder>(TodoListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodoListViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoListViewHolder -> {
                val todoList = getItem(position)
                holder.bind(todoList, menuOptionListeners)
            }
        }
    }
}
