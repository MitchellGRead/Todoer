package com.example.todoer.ui.createlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateListViewModelFactory(private val repo: CreateListRepo) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateListViewModel::class.java)) {
            return CreateListViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}
