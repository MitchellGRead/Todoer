package com.example.todoer.Utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object Util {
    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        view?.let {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
