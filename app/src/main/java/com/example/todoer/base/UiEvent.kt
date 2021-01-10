package com.example.todoer.base

sealed class UiEvent<T>(private val content: T) {

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

data class SnackbarEvent(
    val shouldDisplay: Boolean
) : UiEvent<Boolean>(shouldDisplay)
