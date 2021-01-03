package com.example.todoer.utils

import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat

object ContextUtils {

    fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun Context.getResColor(@ColorRes colorRes: Int, theme: Theme? = null): Int {
        return ResourcesCompat.getColor(this.resources, colorRes, theme)
    }

    fun Context.getResDrawable(@DrawableRes drawableRes: Int, theme: Theme? = null): Drawable? {
        return ResourcesCompat.getDrawable(this.resources, drawableRes, theme)
    }
}
