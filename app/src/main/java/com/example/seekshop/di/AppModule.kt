package com.example.seekshop.di

import android.content.Context
import com.example.seekshop.ui.AppPermissionChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providePermissionChecker(@ApplicationContext context: Context): AppPermissionChecker {
        return AppPermissionChecker(context)
    }
}