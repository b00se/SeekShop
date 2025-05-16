package com.example.seekshop.di

import com.example.seekshop.providers.FusedLocationProvider
import com.example.seekshop.providers.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationBindingModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(
        impl: FusedLocationProvider
    ): LocationProvider
}