package com.example.todoer.ui.homescreen.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.R
import com.example.todoer.database.models.TodoList
import com.example.todoer.databinding.TodoListBinding
import timber.log.Timber
import kotlin.math.roundToInt

class TodoListViewHolder private constructor(
    private val binding: TodoListBinding,
    private val context: Context?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoList, menuOptionListeners: TodoListMenuOptionListeners) {
        with (binding) {
            val completedTasks = item.completedTasks
            val totalTasks = item.totalTasks
            val progress = if (completedTasks != 0) {
                ((completedTasks.toDouble() / totalTasks.toDouble()) * 100).roundToInt()
            } else {
                0
            }

            listTitle.text = item.listName
            todoCountsText.text = "$completedTasks / $totalTasks"
            progressBar.progress = progress
            listOptions.setOnClickListener {
                showPopupMenu(listOptions, item.listId, menuOptionListeners)
            }
        }
    }

    private fun showPopupMenu(
        view: ImageView,
        listId: Long,
        menuOptionListeners: TodoListMenuOptionListeners
    ) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_rename -> {
                    Toast.makeText(context, "Rename selected", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.item_delete -> {
                    menuOptionListeners.deleteClickListener(listId)
                    true
                }
                R.id.item_share -> {
                    Toast.makeText(context, "Share selected", Toast.LENGTH_LONG).show()
                    true
                }
                else -> {
                    Timber.e("An invalid menu item was provided: ${it.title}")
                    false
                }
            }
        }
        popupMenu.inflate(R.menu.todo_list_options)
        popupMenu.show()
    }

    companion object {
        fun from(parent: ViewGroup, context: Context?): TodoListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoListBinding.inflate(layoutInflater, parent, false)
            return TodoListViewHolder(binding, context)
        }
    }
}
