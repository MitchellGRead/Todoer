package com.example.todoer.navigation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListDetailNavArgs(
    val listId: Long,
    val listName: String
) : Parcelable

@Parcelize
data class NoteDetailNavArgs(
    val noteId: Long,
    val noteName: String
) : Parcelable
