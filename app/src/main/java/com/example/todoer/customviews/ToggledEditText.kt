package com.example.todoer.customviews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.example.todoer.utils.ContextUtils.hideKeyboard
import com.example.todoer.utils.ContextUtils.showKeyboard
import com.example.todoer.utils.ViewUtils.setMultiLineAndDoneAction

class ToggledEditText(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatEditText(context, attributeSet) {

    init {
        this.setMultiLineAndDoneAction()
        this.isLongClickable = false
        this.disableEditText()
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

    fun enableEditText() {
        isFocusableInTouchMode = true
        requestFocus()
        setOnClickListener {  }
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mOnKeyboardHidden?.invoke()
            this.disableEditText()
        }
        return true
    }

    override fun onEditorAction(actionCode: Int) {
        super.onEditorAction(actionCode)
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            mOnKeyboardHidden?.invoke()
            this.disableEditText()
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (focused) {
            context.showKeyboard(this)
        } else {
            context.hideKeyboard(this)
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }
}
