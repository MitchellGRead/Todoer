package com.example.todoer.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object DateTimeUtils {

    fun DateTime.getDateTimeString(): String {
        val isDateToday = this.isToday()

        return if (isDateToday) {
            this.getTimeString()
        } else {
            this.getDateString()
        }
    }

    /*
    * Format example:
    * Jan 04, 2007
    * Dec 7, 2020
    * */
    fun DateTime.getDateString(): String {
        val fmt = DateTimeFormat.forPattern("MMM dd, yyyy")
        return this.toString(fmt)
    }

    /*
    * Format example:
    * Sun, 01:55 PM
    * Mon, 8:48 AM
    * */
    fun DateTime.getTimeString(): String {
        val fmt = DateTimeFormat.forPattern("EE, hh:mm a")
        return this.toString(fmt)
    }

    fun DateTime.isToday(): Boolean {
        val year = this.year
        val month = this.dayOfMonth
        val day = this.dayOfYear

        val currentDay = DateTime()

        return year == currentDay.year && month == currentDay.dayOfMonth && day == currentDay.dayOfYear
    }
}
