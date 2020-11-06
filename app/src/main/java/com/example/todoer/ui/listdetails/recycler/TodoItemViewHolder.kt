package com.example.todoer.ui.listdetails.recycler

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.R
import com.example.todoer.database.models.TodoItem
import com.example.todoer.databinding.TodoItemBinding
import com.example.todoer.utils.ContextUtils.getResColor

class TodoItemViewHolder private constructor(
    private val binding: TodoItemBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoItem, todoItemListeners: TodoItemListeners) {
        with(binding) {
            itemName.text = item.itemName
            itemCheckBox.setOnClickListener {
                todoItemListeners.onCheckboxSelected(item.itemId, itemCheckBox.isChecked)
                updateItem(itemCheckBox.isChecked)
            }
        }
    }

    private fun updateItem(isChecked: Boolean) {
        with(binding) {
            if (isChecked) {
                itemName.paintFlags = itemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG  // Turn on strikethrough
                todoListItem.setBackgroundColor(context.getResColor(R.color.colorLight))
            } else {
                itemName.paintFlags = itemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()  // Turn off strikethrough
                todoListItem.setBackgroundResource(R.drawable.bottom_border_light)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): TodoItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
            return TodoItemViewHolder(binding, parent.context)
        }
    }
}
