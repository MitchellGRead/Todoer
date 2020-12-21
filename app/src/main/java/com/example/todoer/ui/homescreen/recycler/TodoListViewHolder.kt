package com.example.todoer.ui.homescreen.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
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

    fun bind(item: TodoList, cardListeners: TodoCardListeners) {
        with (binding) {
            val completedTasks = item.completedTasks
            val totalTasks = item.totalTasks
            val progress = if (completedTasks != 0) {
                ((completedTasks.toDouble() / totalTasks.toDouble()) * 100).roundToInt()
            } else {
                0
            }

            todoListCard.setOnClickListener {
                cardListeners.onClickTodoCard(item.listId, item.todoType, item.listName)
            }
            setupTitle(root, listTitle, item ,cardListeners)
            todoCountsText.text = "$completedTasks / $totalTasks"
            progressBar.progress = progress
            listOptions.setOnClickListener {
                showPopupMenu(listOptions, item, cardListeners, listTitle)
            }
        }
    }

    private fun setupTitle(
        root: View,
        listTitle: ToggledEditText,
        item: TodoList,
        cardListeners: TodoCardListeners
    ) {
        listTitle.setText(item.listName)
        listTitle.rootView = root  // For passing click events to the root
        listTitle.setOnKeyboardHidden {
            onListRename(listTitle, item, cardListeners)
        }
    }

    private fun onListRename(listTitle: EditText, item: TodoList, cardListeners: TodoCardListeners) {
        val updatedName = listTitle.text.toString()
        cardListeners.renameTodoListener(item.listId, item.todoType, updatedName)
    }

    private fun showPopupMenu(
        view: ImageView,
        item: TodoList,
        cardListeners: TodoCardListeners,
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
                    cardListeners.deleteTodoListener(item.listId, item.todoType)
                    true
                }
                else -> {
                    Timber.e("An invalid menu item was provided: ${it.title}")
                    false
                }
            }
        }
        popupMenu.inflate(R.menu.todo_card_options)
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
