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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(FragmentComponent::class)
object RepositoryModule {

    @Provides
    @FragmentScoped
    fun provideHomeScreenRepo(
        todoListDao: TodoListDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): HomeScreenRepo {
        return HomeScreenRepo(todoListDao, dispatcher)
    }

    @Provides
    @FragmentScoped
    fun provideCreateListRepo(
        todoListDao: TodoListDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CreateListRepo {
        return CreateListRepo(todoListDao, dispatcher)
    }

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
