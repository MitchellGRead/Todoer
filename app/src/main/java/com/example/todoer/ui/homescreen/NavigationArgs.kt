package com.example.todoer.ui.homescreen

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListDetailArgs(
    val listId: Long,
    val listName: String
) : Parcelable
