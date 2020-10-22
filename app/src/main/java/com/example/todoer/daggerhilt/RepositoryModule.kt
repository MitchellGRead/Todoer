package com.example.todoer.daggerhilt

import com.example.todoer.database.TodoListDao
import com.example.todoer.ui.homescreen.HomeScreenRepo
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
}
