package com.example.todoer.ui.homescreen.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.R
import com.example.todoer.customviews.ToggledEditText
import com.example.todoer.database.models.TodoList
import com.example.todoer.databinding.TodoListBinding
import timber.log.Timber
import kotlin.math.roundToInt

class TodoListViewHolder private constructor(
    private val binding: TodoListBinding,
    private val context: Context?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoList, listListeners: TodoListListeners) {
        with (binding) {
            val completedTasks = item.completedTasks
            val totalTasks = item.totalTasks
            val progress = if (completedTasks != 0) {
                ((completedTasks.toDouble() / totalTasks.toDouble()) * 100).roundToInt()
            } else {
                0
            }

            todoListCard.setOnClickListener { listListeners.onClickList(item.listId, item.listName) }
            setupTitle(root, listTitle, item ,listListeners)
            todoCountsText.text = "$completedTasks / $totalTasks"
            progressBar.progress = progress
            listOptions.setOnClickListener {
                showPopupMenu(listOptions, item.listId, listListeners, listTitle)
            }
        }
    }

    private fun setupTitle(
        root: View,
        listTitle: ToggledEditText,
        item: TodoList,
        listListeners: TodoListListeners
    ) {
        listTitle.setText(item.listName)
        listTitle.rootView = root  // For passing click events to the root
        listTitle.setOnKeyboardHidden {
            onListRename(listTitle, item.listId, listListeners)
        }
    }

    private fun onListRename(listTitle: EditText, listId: Long, listListeners: TodoListListeners) {
        val updatedName = listTitle.text.toString()
        listListeners.renameClickListener(listId, updatedName)
    }

    private fun showPopupMenu(
        view: ImageView,
        listId: Long,
        listListeners: TodoListListeners,
        listTitle: ToggledEditText
    ) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_rename -> {
                    listTitle.enableEditText()
                    true
                }
                R.id.item_delete -> {
                    listListeners.deleteClickListener(listId)
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
