package com.example.todoer.database

import androidx.room.TypeConverter
import java.util.*

class RoomTypeConverters {

    @TypeConverter
    fun fromTimeStamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time
}
