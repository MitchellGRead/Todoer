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
import com.example.todoer.databinding.TodoNoteBinding
import com.example.todoer.utils.ContextUtils.getResDrawable
import timber.log.Timber

class TodoNoteViewHolder private constructor(
    private val binding: TodoNoteBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NoteItem, cardListeners: TodoCardListeners) {
        with (binding) {
            todoNoteCard.setOnClickListener {
                cardListeners.onClickTodoCard(item)
            }
            cardOptions.setOnClickListener {
                showPopupMenu(item, cardListeners)
            }

            setupTitle(item, cardListeners)
            setupFavourited(item, cardListeners)

            editedDate.text = context.resources.getString(
                R.string.card_edited_date,
                item.editedString
            )

            notePreview.text = item.note.noteDescription
        }
    }

    private fun setupTitle(
        item: NoteItem,
        cardListeners: TodoCardListeners
    ) {
        with(binding) {
            noteTitle.setText(item.note.noteName)
            noteTitle.rootView = root  // For passing click events to the root
            noteTitle.setOnKeyboardHidden {
                onListRename(noteTitle, item, cardListeners)
            }
        }
    }

    private fun setupFavourited(
        item: NoteItem,
        cardListeners: TodoCardListeners
    ) {
        with(binding) {
            val isFavourited = item.note.isFavourited
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

    private fun onListRename(noteTitle: EditText, item: NoteItem, cardListeners: TodoCardListeners) {
        val updatedName = noteTitle.text.toString()
        cardListeners.renameTodoListener(item, updatedName)
    }

    private fun showPopupMenu(
        item: NoteItem,
        cardListeners: TodoCardListeners
    ) {
        val popupMenu = PopupMenu(context, binding.cardOptions)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_rename -> {
                    binding.noteTitle.enableEditText()
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
        fun from(parent: ViewGroup): TodoNoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoNoteBinding.inflate(layoutInflater, parent, false)
            return TodoNoteViewHolder(binding, parent.context)
        }
    }
}
