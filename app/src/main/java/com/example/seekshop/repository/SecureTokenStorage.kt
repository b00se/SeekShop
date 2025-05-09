package com.example.seekshop.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureTokenStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ISecureTokenStorage {

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val TOKEN_EXPIRATION_KEY = longPreferencesKey("token_expiration")

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun saveTokenWithExpiration(token: String, expiration: Long) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[TOKEN_EXPIRATION_KEY] = expiration
        }
    }

    override val tokenFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    override val tokenExpirationFlow: Flow<Long?> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_EXPIRATION_KEY]
        }

}