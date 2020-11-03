package com.example.todoer.utils

import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText

object ViewUtils {

    fun EditText.setMultiLineAndDoneAction() {
        imeOptions = EditorInfo.IME_ACTION_DONE
        setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
    }
}
