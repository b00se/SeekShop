package com.example.seekshop

import android.content.Context
import androidx.startup.Initializer
import com.example.seekshop.repository.AuthRepository
import com.example.seekshop.repository.SecureTokenStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthInitializer : Initializer<Unit> {

    @OptIn(DelicateCoroutinesApi::class)
    override fun create(context: Context) {
        GlobalScope.launch {
            val authRepository = AuthRepository(
                SecureTokenStorage(context),
                com.example.seekshop.network.RetrofitClient()
            )
            authRepository.getAuthToken()
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return listOf(SecureTokenStorageInitializer::class.java).toMutableList()
    }
}