package com.example.todoer.ui.createlist

import timber.log.Timber
import java.util.*

sealed class ListType {
    companion object {
        fun toListType(type: String): ListType {
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

        fun getDefaultName(listType: ListType): String {
            return when (listType) {
                is CheckList -> "New List"
                is Note -> "New Note"
            }
        }
    }
}

data class CheckList(val value: String) : ListType()
data class Note(val value: String) : ListType()
