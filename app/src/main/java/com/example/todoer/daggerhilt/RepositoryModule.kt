package com.example.todoer.daggerhilt

import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.TodoListDao
import com.example.todoer.ui.createlist.CreateListRepo
import com.example.todoer.ui.homescreen.HomeScreenRepo
import com.example.todoer.ui.listdetails.ListDetailsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object RepositoryModule {

    @Provides
    @FragmentScoped
    fun provideHomeScreenRepo(todoListDao: TodoListDao): HomeScreenRepo {
        return HomeScreenRepo(todoListDao)
    }

    @Provides
    @FragmentScoped
    fun provideCreateListRepo(todoListDao: TodoListDao): CreateListRepo {
        return CreateListRepo(todoListDao)
    }

    @Provides
    @FragmentScoped
    fun provideListDetailsRepo(todoItemDao: TodoItemDao): ListDetailsRepo {
        return ListDetailsRepo(todoItemDao)
    }
}
