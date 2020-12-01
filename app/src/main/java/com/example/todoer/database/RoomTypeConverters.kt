package com.example.todoer.database

import androidx.room.TypeConverter
import com.example.todoer.ui.createlist.CheckList
import com.example.todoer.ui.createlist.ListType
import com.example.todoer.ui.createlist.Note
import java.util.*

class RoomTypeConverters {

    @TypeConverter
    fun fromTimeStamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun fromListType(value: String): ListType {
        return ListType.toListType(value)
    }

    @TypeConverter
    fun listTypeToString(listType: ListType): String {
        return when (listType) {
            is CheckList -> listType.value
            is Note -> listType.value
        }
    }
}
