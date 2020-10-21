package com.example.todoer.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}
