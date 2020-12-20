package com.example.todoer.ui.homescreen.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.database.models.TodoNote
import com.example.todoer.databinding.TodoNoteBinding

class TodoNoteViewHolder private constructor(
    private val binding: TodoNoteBinding,
    private val context: Context?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoNote, listListeners: TodoListListeners) {
        with (binding) {
            noteTitle.setText(item.noteName)
            notePreview.text = item.noteDescription
        }
    }

    companion object {
        fun from(parent: ViewGroup, context: Context?): TodoNoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TodoNoteBinding.inflate(layoutInflater, parent, false)
            return TodoNoteViewHolder(binding, context)
        }
    }
}
