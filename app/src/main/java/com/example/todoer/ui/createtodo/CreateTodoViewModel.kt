package com.example.todoer.ui.createtodo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.navigation.ListDetailNavArgs
import kotlinx.coroutines.launch

class CreateTodoViewModel @ViewModelInject constructor(
    private val listRepo: TodoListRepo
) : ViewModel() {

    private val _navigateToTodoList: MutableLiveData<ListDetailNavArgs?> = MutableLiveData()
    val navigateToTodoList: LiveData<ListDetailNavArgs?>
        get() = _navigateToTodoList

    fun onCreateTodo(name: String, type: String) {
        val todoType = TodoType.toListType(type)
        val todoName = if(name.isEmpty()) TodoType.getDefaultName(todoType) else name

        viewModelScope.launch {
            val todoId = when (todoType) {
                is CheckList -> listRepo.insertList(todoName, todoType)
                is Note -> TODO()
            }
            _navigateToTodoList.value = ListDetailNavArgs(
                listId = todoId,
                listName = todoName
            )
        }
    }

    fun onTodoListNavigated() {
        _navigateToTodoList.value = null
    }
}
