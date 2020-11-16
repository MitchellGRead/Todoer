package com.example.todoer

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.example.todoer.utils.ActivityUtils

class ToggledEditText(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatEditText(context, attributeSet) {

    init {
        imeOptions = EditorInfo.IME_ACTION_DONE
        setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
    }

    private var mOnKeyboardHidden: (() -> Unit)? = null
    private var mRoot: View? = null

    fun setOnKeyboardHidden(action: () -> Unit) {
        mOnKeyboardHidden = action
    }

    fun setRootView(root: View) {
        mRoot = root
    }

    fun disableEditText() {
        isFocusable = false
        setOnClickListener { mRoot?.performClick() }
    }

    fun enableEditText(context: Context?) {
        isFocusableInTouchMode = true
        requestFocus()
        setOnClickListener {  }
        ActivityUtils.showKeyboard(context as Activity)
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            hideKeyboard()
            mOnKeyboardHidden?.invoke()
            this.disableEditText()
        }
        return true
    }

    override fun onEditorAction(actionCode: Int) {
        super.onEditorAction(actionCode)
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            mOnKeyboardHidden?.invoke()
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}
