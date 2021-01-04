package com.example.todoer.base

import androidx.fragment.app.Fragment
import com.example.todoer.utils.ContextUtils.hideKeyboard

abstract class BaseFragment : Fragment() {


    override fun onStop() {
        super.onStop()
        activity?.currentFocus?.let { context?.hideKeyboard(it) }
    }
}
