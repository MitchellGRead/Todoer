package com.example.todoer.daggerhilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.*
import javax.inject.Qualifier

@Module
@InstallIn(ApplicationComponent::class)
object CoroutineModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @UiScope
    fun provideUiScope(): CoroutineScope =
        CoroutineScope(Dispatchers.Main + Job())

    @Provides
    @AppIoScope
    fun provideAppIoScope(): CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob())
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UiScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppIoScope
