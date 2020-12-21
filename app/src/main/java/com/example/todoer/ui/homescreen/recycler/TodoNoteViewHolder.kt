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
import com.example.todoer.database.models.TodoNote
import com.example.todoer.databinding.TodoNoteBinding
import timber.log.Timber

class TodoNoteViewHolder private constructor(
    private val binding: TodoNoteBinding,
    private val context: Context?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoNote, cardListeners: TodoCardListeners) {
        with (binding) {
            todoNoteCard.setOnClickListener {
                cardListeners.onClickTodoCard(item.noteId, item.todoType, item.noteName)
            }

            setupTitle(root, noteTitle, item, cardListeners)
            notePreview.text = item.noteDescription
            listOptions.setOnClickListener {
                showPopupMenu(listOptions, item, cardListeners, noteTitle)
            }
        }
    }

    private fun setupTitle(
        root: View,
        noteTitle: ToggledEditText,
        item: TodoNote,
        cardListeners: TodoCardListeners
    ) {
        noteTitle.setText(item.noteName)
        noteTitle.rootView = root  // For passing click events to the root
        noteTitle.setOnKeyboardHidden {
            onListRename(noteTitle, item, cardListeners)
        }
    }

    private fun onListRename(noteTitle: EditText, item: TodoNote, cardListeners: TodoCardListeners) {
        val updatedName = noteTitle.text.toString()
        cardListeners.renameTodoListener(item.noteId, item.todoType, updatedName)
    }

    private fun showPopupMenu(
        view: ImageView,
        item: TodoNote,
        cardListeners: TodoCardListeners,
        noteTitle: ToggledEditText
    ) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_rename -> {
                    noteTitle.enableEditText()
                    true
                }
                R.id.item_delete -> {
                    cardListeners.deleteTodoListener(item.noteId, item.todoType)
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
        fun from(parent: ViewGroup, context: Context?): TodoNoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoNoteBinding.inflate(layoutInflater, parent, false)
            return TodoNoteViewHolder(binding, context)
        }
    }
}
