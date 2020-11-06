package com.example.todoer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

object ContextUtils {

    fun Context.getResColor(@ColorRes color: Int, theme: Theme? = null): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.resources.getColor(color, theme)
        } else {
            @Suppress("DEPRECATION")
            this.resources.getColor(color)
        }
    }
}
