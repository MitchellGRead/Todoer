package com.example.todoer.ui.createtodo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.database.models.TodoNote
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import com.example.todoer.navigation.ListDetailNavArgs
import com.example.todoer.navigation.NoteDetailNavArgs
import kotlinx.coroutines.launch

class CreateTodoViewModel @ViewModelInject constructor(
    private val listRepo: TodoListRepo,
    private val noteRepo: TodoNoteRepo
) : ViewModel() {

    private val _navigateToTodoList: MutableLiveData<ListDetailNavArgs?> = MutableLiveData()
    val navigateToTodoList: LiveData<ListDetailNavArgs?>
        get() = _navigateToTodoList

    private val _navigateToTodoNote: MutableLiveData<NoteDetailNavArgs?> = MutableLiveData()
    val navigateToTodoNote: LiveData<NoteDetailNavArgs?>
        get() = _navigateToTodoNote

    fun onCreateTodo(name: String, type: String) {
        val todoType = TodoType.toListType(type)
        val todoName = if(name.isEmpty()) TodoType.getDefaultName(todoType) else name

        viewModelScope.launch {
            when (todoType) {
                is CheckList -> {
                    val listId = listRepo.insertList(todoName)
                    _navigateToTodoList.value = ListDetailNavArgs(
                        listId = listId,
                        listName = todoName
                    )
                }
                is Note -> {
                    val noteId = noteRepo.insertNote(todoName)
                    _navigateToTodoNote.value = NoteDetailNavArgs(
                        noteId = noteId,
                        noteName = todoName
                    )
                }
            }

        }
    }

    fun onTodoListNavigated() {
        _navigateToTodoList.value = null
    }

    fun onTodoNoteNavigated() {
        _navigateToTodoNote.value = null
    }
}
