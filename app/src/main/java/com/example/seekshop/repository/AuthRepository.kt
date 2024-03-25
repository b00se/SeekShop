package com.example.seekshop.repository

import com.example.seekshop.network.RetrofitClient
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val secureTokenStorage: SecureTokenStorage,
    private val authServe: RetrofitClient
) {

    suspend fun getAuthToken(): String {
        secureTokenStorage.tokenFlow.firstOrNull()?.let { if (isTokenValid()) return it }
        return fetchAndStoreNewToken()
    }

    private suspend fun fetchAndStoreNewToken(): String {
        val newToken = authServe.fetchAuthToken().getOrThrow()
        val expirationTime = System.currentTimeMillis() + (newToken.expiresIn.toLong() * 1000L)
        secureTokenStorage.saveTokenWithExpiration(newToken.accessToken, expirationTime)
        return newToken.accessToken
    }

    private suspend fun isTokenValid(): Boolean {
        val expirationTime = secureTokenStorage.tokenExpirationFlow.firstOrNull() ?: return false
        return System.currentTimeMillis() < expirationTime
    }
}