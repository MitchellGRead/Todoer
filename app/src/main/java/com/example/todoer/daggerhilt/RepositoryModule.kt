package com.example.todoer.daggerhilt

import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.TodoListDao
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.ui.listdetails.ListDetailsRepo
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
    fun provideListDetailsRepo(
        todoListDao: TodoListDao,
        todoItemDao: TodoItemDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ListDetailsRepo {
        return ListDetailsRepo(todoListDao, todoItemDao, dispatcher)
    }
}
