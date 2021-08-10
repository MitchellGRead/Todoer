package com.example.todoer.ui.homescreen

sealed class HomeAction
data class SnackbarAction(val shouldShow: Boolean) : HomeAction()
data class ShareAction(val data: String) : HomeAction()
