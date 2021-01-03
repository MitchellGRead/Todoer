package com.example.todoer.database

import androidx.room.TypeConverter
import com.example.todoer.ui.createtodo.CheckList
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.createtodo.Note
import org.joda.time.DateTime

class RoomTypeConverters {

    @TypeConverter
    fun millisToDateTime(value: Long): DateTime = DateTime(value)

    @TypeConverter
    fun dateTimeToMillis(date: DateTime): Long = date.millis

    @TypeConverter
    fun typeIdToTodoType(id: String): TodoType {
        return TodoType.toTodoType(id)
    }

    @TypeConverter
    fun todoTypeToTypeId(todoType: TodoType): String {
        return when (todoType) {
            is CheckList -> todoType.id
            is Note -> todoType.id
        }
    }
}
