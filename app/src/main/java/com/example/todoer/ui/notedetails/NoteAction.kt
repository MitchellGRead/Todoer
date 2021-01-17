package com.example.todoer.ui.notedetails

sealed class NoteAction
data class ShareNote(val data: String) : NoteAction()
