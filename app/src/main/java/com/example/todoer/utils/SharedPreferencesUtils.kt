package com.example.todoer.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences

object SharedPreferencesUtils {

    fun SharedPreferences?.getBooleanValue(key: String, default: Boolean = false): Boolean {
        if (this == null) return default

        return this.getBoolean(key, default)
    }

    fun SharedPreferences?.setBooleanValue(key: String, value: Boolean) {
        if (this == null) return

        this.edit().putBoolean(key, value).apply()
    }
}
