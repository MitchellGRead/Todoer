package com.example.todoer.ui.homescreen.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class HomeScreenAdapter(
    private val cardListeners: TodoCardListeners,
    private val context: Context?
) : ListAdapter<HomeScreenItem, RecyclerView.ViewHolder>(HomeScreenDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HomeScreenItem.ITEM_VIEW_TYPE_CHECKLIST -> TodoListViewHolder.from(parent, context)
            HomeScreenItem.ITEM_VIEW_TYPE_NOTE -> TodoNoteViewHolder.from(parent, context)
            else -> throw ClassCastException("Unknown viewtype: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoListViewHolder -> {
                val listItem = getItem(position) as ChecklistItem
                holder.bind(listItem, cardListeners)
            }
            is TodoNoteViewHolder -> {
                val noteItem = getItem(position) as NoteItem
                holder.bind(noteItem, cardListeners)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChecklistItem -> HomeScreenItem.ITEM_VIEW_TYPE_CHECKLIST
            is NoteItem -> HomeScreenItem.ITEM_VIEW_TYPE_NOTE
        }
    }
}
