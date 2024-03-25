package com.example.seekshop

import android.content.Context
import androidx.startup.Initializer
import com.example.seekshop.repository.SecureTokenStorage

class SecureTokenStorageInitializer : Initializer<SecureTokenStorage> {

    override fun create(context: Context): SecureTokenStorage {
        SecureTokenStorage.initialize(context)
        return SecureTokenStorage.getInstance(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}