package com.example.seekshop.network

import com.example.seekshop.network.model.AuthTokenResponse
import com.example.seekshop.network.model.LocationsResponse
import com.example.seekshop.network.model.ProductResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface KrogerService {
    @FormUrlEncoded
    @POST("v1/connect/oauth2/token")
    suspend fun getAuthToken(
        @Header("Authorization") authHeader: String,
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "product.compact",
    ): Response<AuthTokenResponse>

    @GET("v1/locations")
    suspend fun getLocations(
        @Header("Authorization") authHeader: String,
        @Query("filter.zipCode.near") zipCode: String? = null,
        @Query("filter.latLong.near") latLong: String? = null,
        @Query("filter.limit") limit: Int = 10,
    ): Response<LocationsResponse>

    @GET("v1/products")
    suspend fun getProduct(
        @Header("Authorization") authHeader: String,
        @Query("filter.locationId") locationId: String,
        @Query("filter.term") term: String,
        @Query("filter.limit") limit: Int = 50,
    ): Response<ProductResponse>
}
