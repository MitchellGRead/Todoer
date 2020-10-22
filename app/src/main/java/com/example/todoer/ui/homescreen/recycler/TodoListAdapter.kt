package com.example.todoer.ui.homescreen.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.database.models.TodoList
import com.example.todoer.databinding.TodoListBinding
import kotlin.math.roundToInt

class TodoListAdapter : ListAdapter<TodoList, RecyclerView.ViewHolder>(TodoListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodoListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoListViewHolder -> {
                val todoList = getItem(position)
                holder.bind(todoList)
            }
        }
    }
}

class TodoListViewHolder private constructor(
    private val binding: TodoListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoList) {
        with (binding) {
            listTitle.text = item.listName
            todoCountsText.text = "${item.completedTasks} / ${item.totalTasks}"
            progressBar.progress = ((item.completedTasks.toDouble() / item.totalTasks.toDouble()) * 100).roundToInt()
//            listOptions.setOnClickListener {  }
        }
    }

    companion object {
        fun from(parent: ViewGroup): TodoListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoListBinding.inflate(layoutInflater, parent, false)
            return TodoListViewHolder(binding)
        }
    }
}
