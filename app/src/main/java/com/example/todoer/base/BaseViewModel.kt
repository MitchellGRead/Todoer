package com.example.todoer.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<A> : ViewModel() {

    val action = MutableLiveData<ViewModelAction<A>>()

    fun setAction(a: A) {
        action.postValue(ViewModelAction(a))
    }
}
