package com.example.todoer.database

import androidx.room.TypeConverter
import com.example.todoer.ui.createtodo.CheckList
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.createtodo.Note
import java.util.*

class RoomTypeConverters {

    @TypeConverter
    fun fromTimeStamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun fromListType(value: String): TodoType {
        return TodoType.toListType(value)
    }

    @TypeConverter
    fun listTypeToString(todoType: TodoType): String {
        return when (todoType) {
            is CheckList -> todoType.value
            is Note -> todoType.value
        }
    }
}
