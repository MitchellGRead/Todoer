package com.example.todoer.daggerhilt

import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.TodoNoteDao
import com.example.todoer.domain.TodoItemRepo
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.domain.TodoNoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(FragmentComponent::class)
object RepositoryModule {

    @Provides
    @FragmentScoped
    fun provideTodoListRepo(
        todoListDao: TodoListDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ) : TodoListRepo = TodoListRepo(todoListDao, dispatcher)

    @Provides
    @FragmentScoped
    fun provideTodoNoteRepo(
        todoNoteDao: TodoNoteDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ) : TodoNoteRepo = TodoNoteRepo(todoNoteDao, dispatcher)

    @Provides
    @FragmentScoped
    fun provideTodoItemRepo(
        todoItemDao: TodoItemDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ) : TodoItemRepo = TodoItemRepo(todoItemDao, dispatcher)
}
