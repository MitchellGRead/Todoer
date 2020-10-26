package com.example.todoer.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class HomeScreenViewModel @ViewModelInject constructor(
    private val repo: HomeScreenRepo
) : ViewModel() {

    val todoLists = repo.fetchTodoLists()

    private val _navigateToCreateList: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToCreateList: LiveData<Boolean>
        get() = _navigateToCreateList

    init {
        Timber.d("Init HomeScreen ViewModel")
    }

    /* Navigation Functions */
    fun onFabButtonClicked() {
        _navigateToCreateList.value = true
    }

    fun onCreateListNavigated() {
        _navigateToCreateList.value = false
    }
}
