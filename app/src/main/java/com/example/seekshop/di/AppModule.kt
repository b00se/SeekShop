package com.example.seekshop.di

import android.content.Context
import com.example.seekshop.providers.FusedLocationProvider
import com.example.seekshop.providers.LocationProvider
import com.example.seekshop.repository.LocationRepository
import com.example.seekshop.ui.AppPermissionChecker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLocationRepository() : LocationRepository {
        return LocationRepository()
    }
}