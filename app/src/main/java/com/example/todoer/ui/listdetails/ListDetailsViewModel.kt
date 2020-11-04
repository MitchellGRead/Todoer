package com.example.todoer.ui.listdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class ListDetailsViewModel @AssistedInject constructor(
    @Assisted private val listId: Long,
    private val repo: ListDetailsRepo
) : ViewModel() {

    val todoItems = repo.fetchTodoItems(listId)

    fun insertTodoItem(itemName: String) {
        viewModelScope.launch {
            repo.insertTodoItem(listId, itemName)
        }
    }

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(listId: Long): ListDetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            listId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(listId) as T
            }
        }
    }
}
