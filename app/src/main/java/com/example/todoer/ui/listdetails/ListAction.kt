package com.example.todoer.ui.listdetails

sealed class ListAction
data class SnackbarAction(val shouldShow: Boolean) : ListAction()
data class ShareAction(val data: String) : ListAction()
