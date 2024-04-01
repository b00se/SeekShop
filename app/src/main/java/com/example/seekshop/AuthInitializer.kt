package com.example.seekshop

import android.content.Context
import androidx.startup.Initializer
import com.example.seekshop.network.RetrofitClient
import com.example.seekshop.repository.AuthRepository
import com.example.seekshop.repository.SecureTokenStorage
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AuthInitializerEntryPoint {
    fun getSecureTokenStorage(): SecureTokenStorage
    fun getRetrofitClient(): RetrofitClient
}

class AuthInitializer : Initializer<Unit> {

    @OptIn(DelicateCoroutinesApi::class)
    override fun create(context: Context) {
        val appEntryPoint = EntryPoints.get(context, AuthInitializerEntryPoint::class.java)
        val secureTokenStorage = appEntryPoint.getSecureTokenStorage()
        val retrofitClient = appEntryPoint.getRetrofitClient()

        GlobalScope.launch {
            val authRepository = AuthRepository(
                secureTokenStorage,
                retrofitClient
            )
            authRepository.getAuthToken()
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}