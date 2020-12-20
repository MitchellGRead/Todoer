package com.example.todoer.ui.homescreen.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.todoer.database.models.TodoList

class HomeScreenDiffCallback : DiffUtil.ItemCallback<HomeScreenItem>() {

    override fun areItemsTheSame(oldItem: HomeScreenItem, newItem: HomeScreenItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HomeScreenItem, newItem: HomeScreenItem): Boolean {
        return oldItem == newItem
    }
}
