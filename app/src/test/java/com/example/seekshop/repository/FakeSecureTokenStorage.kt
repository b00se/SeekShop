package com.example.seekshop.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSecureTokenStorage : ISecureTokenStorage {

    private val _tokenFlow = MutableStateFlow<String?>(null)
    private val _tokenExpirationFlow = MutableStateFlow<Long?>(null)

    override val tokenFlow: StateFlow<String?> get() = _tokenFlow
    override val tokenExpirationFlow: StateFlow<Long?> get() = _tokenExpirationFlow
    override suspend fun saveToken(token: String) {
    }

    override suspend fun saveTokenWithExpiration(token: String, expiration: Long) {
        _tokenFlow.value = token
        _tokenExpirationFlow.value = expiration
    }

}