package com.example.todoer.ui.notedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoNoteRepo
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber

class NoteDetailsViewModel @AssistedInject constructor(
    @Assisted private val noteId: Long,
    private val noteRepo: TodoNoteRepo
) : ViewModel() {

    init {
        Timber.d("NoteDetailsViewModel created")
    }

    suspend fun getNoteDescription(): String {
        return noteRepo.getNoteDescription(noteId) ?: ""
    }

    fun saveNoteDescription(updatedDescription: String) {
        viewModelScope.launch {
            noteRepo.updateNoteDescription(noteId, updatedDescription)
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
