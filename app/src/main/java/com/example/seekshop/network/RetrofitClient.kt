package com.example.seekshop.network

import android.util.Base64
import android.util.Base64.encodeToString
import com.example.seekshop.BuildConfig
import com.example.seekshop.network.model.AuthTokenResponse
import com.example.seekshop.network.model.LocationsResponse
import com.example.seekshop.network.model.ProductResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor(){
    private val contentType = "application/json".toMediaType()

    private val json = Json {
        ignoreUnknownKeys = true
    }


    private val instance: Retrofit by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(contentType)
            )
            .build()
    }

    suspend fun fetchAuthToken(): Result<AuthTokenResponse> {
        val clientId = BuildConfig.KROGER_CLIENT_ID
        val clientSecret = BuildConfig.KROGER_CLIENT_SECRET
        val credentials = "$clientId:$clientSecret"
        val authHeader = "Basic ${encodeToString(credentials.toByteArray(), Base64.NO_WRAP)}"

        val service = instance.create(KrogerService::class.java)

        try {
            val response = service.getAuthToken(authHeader)
            response.body()?.let {
                return Result.success(it)
            } ?: run {
                return Result.failure(RuntimeException("Failed to fetch auth token"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun fetchLocations(
        authToken: String,
        zipCode: String? = null,
        latLong: String? = null,
    ): Result<LocationsResponse> {
        val service = instance.create(KrogerService::class.java)

        if (zipCode == null && latLong == null) {
            return Result.failure(RuntimeException("Either zipCode or latLon must be provided"))
        }

        try {
            val response = service.getLocations(
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

    suspend fun fetchProduct(
        authToken : String,
        locationId : String,
        term : String,
    ) : Result<ProductResponse> {
        val service = instance.create(KrogerService::class.java)

        try {
            val response = service.getProduct(
                authHeader = "Bearer $authToken",
                locationId = locationId,
                term = term
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