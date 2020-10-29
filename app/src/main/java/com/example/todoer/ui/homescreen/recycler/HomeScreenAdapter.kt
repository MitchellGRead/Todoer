package com.example.todoer.ui.homescreen.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.database.models.TodoList

class HomeScreenAdapter(
    private val menuOptionListeners: TodoListMenuOptionListeners
) : ListAdapter<TodoList, RecyclerView.ViewHolder>(HomeScreenDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodoListViewHolder.from(parent)
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
