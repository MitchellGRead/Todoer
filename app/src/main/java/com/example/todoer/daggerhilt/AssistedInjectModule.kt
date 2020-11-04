package com.example.todoer.daggerhilt

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@AssistedModule
@Module
@InstallIn(FragmentComponent::class)
interface AssistedInjectModule {}
