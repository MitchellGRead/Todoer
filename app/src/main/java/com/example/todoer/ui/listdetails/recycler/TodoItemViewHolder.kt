package com.example.todoer.ui.listdetails.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.database.models.TodoItem
import com.example.todoer.databinding.TodoItemBinding

class TodoItemViewHolder private constructor(
    private val binding: TodoItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoItem) {
        with(binding) {
            itemName.setText(item.itemName)
        }
    }

    companion object {
        fun from(parent: ViewGroup): TodoItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
            return TodoItemViewHolder(binding)
        }
    }
}
