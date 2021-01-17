package com.example.todoer.daggerhilt

import com.example.todoer.sharing.ShareIntentFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    @FragmentScoped
    fun providesShareIntentFactory(): ShareIntentFactory {
        return ShareIntentFactory()
    }
}
