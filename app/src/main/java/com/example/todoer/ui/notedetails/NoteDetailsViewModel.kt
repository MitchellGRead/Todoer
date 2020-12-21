package com.example.todoer.ui.notedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoer.domain.TodoNoteRepo
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class NoteDetailsViewModel @AssistedInject constructor(
    @Assisted private val noteId: Long,
    private val noteRepo: TodoNoteRepo
) : ViewModel() {

    fun observeNoteContent(): String {
        return ""
    }


    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(noteId: Long): NoteDetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            noteId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(noteId) as T
            }
        }
    }
}
