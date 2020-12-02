package com.example.todoer.ui.createtodo

import timber.log.Timber
import java.util.*

sealed class TodoType {
    companion object {
        fun toListType(type: String): TodoType {
            return when (type.toLowerCase(Locale.ROOT)) {
                "checklist" -> CheckList("Checklist")
                "note" -> Note("Note")
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

data class CheckList(val value: String) : TodoType()
data class Note(val value: String) : TodoType()
