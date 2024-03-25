package com.example.seekshop.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.osipxd.security.crypto.createEncrypted
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SecureTokenStorage @Inject constructor( @ApplicationContext private val context: Context) {

    companion object {
        @Volatile private var INSTANCE: SecureTokenStorage? = null

        private lateinit var dataStore: DataStore<Preferences>

        fun initialize(context: Context) {
            dataStore = PreferenceDataStoreFactory.createEncrypted {
                EncryptedFile.Builder(
                    context.applicationContext.dataStoreFile("token_storage.preferences_pb"),
                    context.applicationContext,
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
                ).build()
            }
        }

        fun getInstance(context: Context): SecureTokenStorage {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SecureTokenStorage(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val TOKEN_EXPIRATION_KEY = longPreferencesKey("token_expiration")

    private val dataStore: DataStore<Preferences>
        get() = Companion.dataStore

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveTokenWithExpiration(token: String, expiration: Long) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[TOKEN_EXPIRATION_KEY] = expiration
        }
    }

    val tokenFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    val tokenExpirationFlow: Flow<Long?> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_EXPIRATION_KEY]
        }

}