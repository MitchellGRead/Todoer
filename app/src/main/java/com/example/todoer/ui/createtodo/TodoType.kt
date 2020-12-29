package com.example.todoer.ui.createtodo

import timber.log.Timber
import java.util.*

sealed class TodoType {
    companion object {
        const val CheckListTypeId = "checklist"
        const val NoteTypeId = "note"

        /*
        Error is thrown on invalid type id cause the user should never have access to the string
        id types themselves. This ensures we can catch and fix the mistake immediately in development.
         */
        fun toTodoType(type: String): TodoType {
            return when (type.toLowerCase(Locale.ROOT)) {
                CheckListTypeId -> CheckList("Checklist")
                NoteTypeId -> Note("Note")
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
data class CheckList(val id: String) : TodoType()
data class Note(val id: String) : TodoType()
