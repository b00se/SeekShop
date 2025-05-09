package com.example.seekshop.di

import com.example.seekshop.network.api.FakeRetrofitClient
import com.example.seekshop.network.api.RetrofitClient
import com.example.seekshop.network.api.RetrofitClientContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TestNetworkModule {

    @Binds
    @Singleton
    abstract fun bindRetrofitClient(fakeRetrofitClient: FakeRetrofitClient): RetrofitClientContract
}