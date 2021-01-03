package com.example.todoer.ui.homescreen.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.R
import com.example.todoer.customviews.ToggledEditText
import com.example.todoer.databinding.TodoListBinding
import com.example.todoer.utils.ContextUtils.getResDrawable
import timber.log.Timber
import kotlin.math.roundToInt

class TodoListViewHolder private constructor(
    private val binding: TodoListBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChecklistItem, cardListeners: TodoCardListeners) {
        with (binding) {
            val todoList = item.checkList

            val completedTasks = todoList.completedTasks
            val totalTasks = todoList.totalTasks
            val progress = if (completedTasks != 0) {
                ((completedTasks.toDouble() / totalTasks.toDouble()) * 100).roundToInt()
            } else {
                0
            }

            todoListCard.setOnClickListener {
                cardListeners.onClickTodoCard(item)
            }
            cardOptions.setOnClickListener {
                showPopupMenu(item, cardListeners)
            }

            setupTitle(item ,cardListeners)
            setupFavourited(item, cardListeners)

            with(context.resources) {
                todoCountsText.text = getString(
                    R.string.checklist_item_complete_over_total_tasks,
                    completedTasks,
                    totalTasks)
                editedDate.text = getString(
                    R.string.card_edited_date,
                    item.editedString
                )
            }

            progressBar.progress = progress
        }
    }

    private fun setupTitle(
        item: ChecklistItem,
        cardListeners: TodoCardListeners
    ) {
        with(binding) {
            listTitle.setText(item.checkList.listName)
            listTitle.rootView = root  // For passing click events to the root
            listTitle.setOnKeyboardHidden {
                onListRename(listTitle, item, cardListeners)
            }
        }
    }

    private fun setupFavourited(
        item: ChecklistItem,
        cardListeners: TodoCardListeners
    ) {
        with(binding) {
            val isFavourited = item.checkList.isFavourited
            favouriteIcon.isChecked = isFavourited
            setFavouriteIcon(isFavourited)

            favouriteIcon.setOnClickListener {
                val toggleIsFavourited = favouriteIcon.isChecked
                cardListeners.onCardFavouritedListener(item, toggleIsFavourited)
                setFavouriteIcon(toggleIsFavourited)
            }
        }
    }

    private fun setFavouriteIcon(isFavourited: Boolean) {
        with(binding) {
            if (isFavourited) {
                favouriteIcon.buttonDrawable = context.getResDrawable(R.drawable.ic_round_filled_star)
            } else {
                favouriteIcon.buttonDrawable = context.getResDrawable(R.drawable.ic_round_star_outline)
            }
        }
    }

    private fun onListRename(listTitle: EditText, item: ChecklistItem, cardListeners: TodoCardListeners) {
        val updatedName = listTitle.text.toString()
        cardListeners.renameTodoListener(item, updatedName)
    }

    private fun showPopupMenu(
        item: ChecklistItem,
        cardListeners: TodoCardListeners
    ) {
        val popupMenu = PopupMenu(context, binding.cardOptions)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_rename -> {
                    binding.listTitle.enableEditText()
                    true
                }
                R.id.item_delete -> {
                    cardListeners.deleteTodoListener(item)
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
        fun from(parent: ViewGroup): TodoListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoListBinding.inflate(layoutInflater, parent, false)
            return TodoListViewHolder(binding, parent.context)
        }
    }
}
