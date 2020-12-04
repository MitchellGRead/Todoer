package com.example.todoer.daggerhilt

import android.app.Application
import androidx.room.Room
import com.example.todoer.database.TodoDatabase
import com.example.todoer.database.TodoItemDao
import com.example.todoer.database.TodoListDao
import com.example.todoer.database.TodoNoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(application: Application): TodoDatabase {
        return Room
            .databaseBuilder(application, TodoDatabase::class.java, "TodoDatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoListDao(todoDatabase: TodoDatabase): TodoListDao {
        return todoDatabase.todoListDao()
    }

    @Provides
    @Singleton
    fun provideTodoNoteDao(todoDatabase: TodoDatabase): TodoNoteDao {
        return todoDatabase.todoNoteDao()
    }

    @Provides
    @Singleton
    fun provideTodoItemDao(todoDatabase: TodoDatabase): TodoItemDao {
        return todoDatabase.todoItemDao()
    }
}
