package com.example.todoer.ui.createtodo

import timber.log.Timber
import java.util.*

sealed class TodoType {
    companion object {
        const val CheckListType = "checklist"
        const val NoteType = "note"

        fun toListType(type: String): TodoType {
            return when (type.toLowerCase(Locale.ROOT)) {
                // TODO(Change this so it doesn't represent what is seen on view)
                CheckListType -> CheckList("Checklist")
                NoteType -> Note("Note")
                else -> {
                    val message = "List type: $type does not exist"
                    Timber.e(message)
                    throw IllegalArgumentException(message)
                }
            }
        }

        fun getDefaultName(todoType: TodoType): String {
            return when (todoType) {
                is CheckList -> "New List"
                is Note -> "New Note"
            }
        }
    }
}

// Best to use TodoType.[const val type] for safety initializing
// TODO(Make this better)
data class CheckList(val value: String) : TodoType()
data class Note(val value: String) : TodoType()
