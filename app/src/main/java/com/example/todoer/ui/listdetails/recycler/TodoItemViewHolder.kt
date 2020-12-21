package com.example.todoer.ui.listdetails.recycler

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.R
import com.example.todoer.customviews.ToggledEditText
import com.example.todoer.database.models.TodoItem
import com.example.todoer.databinding.TodoListItemBinding
import com.example.todoer.utils.ContextUtils.getResColor

class TodoItemViewHolder private constructor(
    private val binding: TodoListItemBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoItem, todoItemListeners: TodoItemListeners) {
        with(binding) {
            itemName.setText(item.itemName)
            itemName.setOnKeyboardHidden {
                onRenameItem(itemName, item.itemId, todoItemListeners)
            }

            itemCompleted(item.isComplete)
            itemCheckBox.setOnClickListener {
                todoItemListeners.onCheckboxSelected(item.itemId, itemCheckBox.isChecked)
                itemCompleted(itemCheckBox.isChecked)
            }

            editItem.setOnClickListener {
                itemName.enableEditText()
            }
            deleteItem.setOnClickListener {
                todoItemListeners.onDeleted(item.itemId)
            }
        }
    }

    private fun onRenameItem(
        itemName: ToggledEditText,
        itemId: Long,
        todoItemListeners: TodoItemListeners
    ) {
        val updatedText = itemName.text.toString()
        todoItemListeners.onEdited(itemId, updatedText)
    }

    private fun itemCompleted(isComplete: Boolean) {
        with(binding) {
            itemCheckBox.isChecked = isComplete

            if (isComplete) {
                itemName.paintFlags = itemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG  // Turn on strikethrough
                todoListItem.setBackgroundColor(context.getResColor(R.color.todoItemCompleteBackground))
            } else {
                itemName.paintFlags = itemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()  // Turn off strikethrough
                todoListItem.setBackgroundResource(R.drawable.bottom_border_primary_dark)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): TodoItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoListItemBinding.inflate(layoutInflater, parent, false)
            return TodoItemViewHolder(binding, parent.context)
        }
    }
}
