package com.example.todoer.ui.notedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoNoteRepo
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteDetailsViewModel @AssistedInject constructor(
    @Assisted private val noteId: Long,
    private val noteRepo: TodoNoteRepo
) : ViewModel() {

    suspend fun getNoteDescription(): String {
        return noteRepo.getNoteDescription(noteId)
    }

    fun saveNoteContent(updatedContent: String) {
        viewModelScope.launch {
            noteRepo.updateNoteDescription(noteId, updatedContent)
        }

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