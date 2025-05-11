package com.example.seekshop.network.api

import com.example.seekshop.domain.model.AuthToken
import com.example.seekshop.network.dto.AuthTokenResponseDTO
import com.example.seekshop.network.dto.LocationsResponseDTO
import com.example.seekshop.network.dto.ProductResponseDTO

interface RetrofitClientContract {
    suspend fun fetchAuthToken(): Result<AuthTokenResponseDTO>
    suspend fun fetchLocations(authToken: String, zipCode: String? = null, latLong: String? = null): Result<LocationsResponseDTO>
    suspend fun fetchProduct(authToken: String, locationId: String, term: String): Result<ProductResponseDTO>
}