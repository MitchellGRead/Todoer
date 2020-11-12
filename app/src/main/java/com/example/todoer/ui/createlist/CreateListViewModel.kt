package com.example.todoer.ui.createlist

import android.app.Activity
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.utils.ActivityUtils
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.launch

class CreateListViewModel @ViewModelInject constructor(
    @ActivityContext private val context: Context,
    private val repo: TodoListRepo
) : ViewModel() {

    private val _navigateToHomeScreen: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToHomeScreen: LiveData<Boolean>
        get() = _navigateToHomeScreen

    fun onCreateList(listName: String) {
        viewModelScope.launch {
            repo.insertList(listName)
            ActivityUtils.hideKeyboard(context as Activity)
            _navigateToHomeScreen.value = true
        }
    }

    fun onHomeScreenNavigated() {
        _navigateToHomeScreen.value = false
    }
}
