package com.example.seekshop.repository

import com.example.seekshop.network.api.RetrofitClient
import com.example.seekshop.network.api.RetrofitClientContract
import com.example.seekshop.network.mappers.toDomain
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val secureTokenStorage: ISecureTokenStorage,
    private val authServe: RetrofitClientContract
) {

    suspend fun getAuthToken(): String {
        secureTokenStorage.tokenFlow.firstOrNull()?.let { if (isTokenValid()) return it }
        return fetchAndStoreNewToken()
    }

    private suspend fun fetchAndStoreNewToken(): String {
        val newToken = authServe.fetchAuthToken().getOrThrow().toDomain()
        val expirationTime = System.currentTimeMillis() + (newToken.expiresIn.toLong() * 1000L)
        secureTokenStorage.saveTokenWithExpiration(newToken.accessToken, expirationTime)
        return newToken.accessToken
    }

    private suspend fun isTokenValid(): Boolean {
        val expirationTime = secureTokenStorage.tokenExpirationFlow.firstOrNull() ?: return false
        return System.currentTimeMillis() < expirationTime
    }
}