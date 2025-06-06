package com.example.seekshop.network.api

import android.util.Base64
import android.util.Base64.encodeToString
import android.util.Log
import com.example.seekshop.BuildConfig
import com.example.seekshop.network.dto.ProductResponseDTO
import com.example.seekshop.network.dto.AuthTokenResponseDTO
import com.example.seekshop.network.dto.LocationsResponseDTO
import com.example.seekshop.network.mappers.toDomain
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val krogerService: KrogerService
) : RetrofitClientContract {

    override suspend fun fetchAuthToken(): Result<AuthTokenResponseDTO> {
        val clientId = BuildConfig.KROGER_CLIENT_ID
        val clientSecret = BuildConfig.KROGER_CLIENT_SECRET
        val credentials = "$clientId:$clientSecret"
        val authHeader = "Basic ${encodeToString(credentials.toByteArray(), Base64.NO_WRAP)}"


        try {
            val response = krogerService.getAuthToken(authHeader)
            response.body()?.let {
                return Result.success(it)
            } ?: run {
                return Result.failure(RuntimeException("Failed to fetch auth token"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun fetchLocations(
        authToken: String,
        zipCode: String?,
        latLong: String?,
    ): Result<LocationsResponseDTO> {

        if (zipCode == null && latLong == null) {
            return Result.failure(RuntimeException("Either zipCode or latLon must be provided"))
        }

        try {
            val response = krogerService.getLocations(
                authHeader = "Bearer $authToken",
                zipCode = zipCode,
                latLong = latLong,
            )
            response.body()?.let {
                return Result.success(it)
            } ?: run {
                return Result.failure(RuntimeException("Failed to fetch locations"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun fetchProduct(
        authToken : String,
        locationId : String,
        term : String,
        limit : Int,
        start : Int,
    ) : Result<ProductResponseDTO> {

        try {
            val response = krogerService.getProduct(
                authHeader = "Bearer $authToken",
                locationId = locationId,
                term = term,
                limit = limit,
                start = start
            )
            response.body()?.let {
                return Result.success(it)
            } ?: run {
                return Result.failure(RuntimeException("Failed to fetch product"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}