package com.example.todoer.base

/*
* This is intended to be a wrapper for viewmodel actions that are fed into viewmodels
* It allows for a single retrieval of content which helps to ensure consistency in livedata
* */
open class ViewModelAction<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
