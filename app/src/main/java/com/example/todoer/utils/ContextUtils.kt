package com.example.todoer.utils

import android.content.Context
import android.content.res.Resources.Theme
import android.os.Build
import android.renderscript.ScriptGroup
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes

object ContextUtils {

    fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun Context.getResColor(@ColorRes color: Int, theme: Theme? = null): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.resources.getColor(color, theme)
        } else {
            @Suppress("DEPRECATION")
            this.resources.getColor(color)
        }
    }
}
