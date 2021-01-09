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
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTodoListRepo(
        todoListDao: TodoListDao,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        @AppIoScope appIoScope: CoroutineScope
    ) : TodoListRepo = TodoListRepo(todoListDao, dispatcher, appIoScope)

    @Provides
    @Singleton
    fun provideTodoNoteRepo(
        todoNoteDao: TodoNoteDao,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        @AppIoScope appIoScope: CoroutineScope
    ) : TodoNoteRepo = TodoNoteRepo(todoNoteDao, dispatcher, appIoScope)

    @Provides
    @Singleton
    fun provideTodoItemRepo(
        todoItemDao: TodoItemDao,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        @AppIoScope appIoScope: CoroutineScope
    ) : TodoItemRepo = TodoItemRepo(todoItemDao, dispatcher, appIoScope)
}
