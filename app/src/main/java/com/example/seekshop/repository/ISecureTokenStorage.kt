package com.example.seekshop.repository

import kotlinx.coroutines.flow.Flow

interface ISecureTokenStorage {
    val tokenFlow: Flow<String?>
    val tokenExpirationFlow: Flow<Long?>

    suspend fun saveToken(token: String)
    suspend fun saveTokenWithExpiration(token: String, expiration: Long)
}