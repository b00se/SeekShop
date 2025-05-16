package com.example.seekshop.providers

interface LocationProvider {
    suspend fun getLatLon(): Result<Pair<String, String>>
}