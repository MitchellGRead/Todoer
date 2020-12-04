package com.example.todoer.domain

import com.example.todoer.daggerhilt.IoDispatcher
import com.example.todoer.database.TodoNoteDao
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TodoNoteRepo @Inject constructor(
    private val todoNoteDao: TodoNoteDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {


}
