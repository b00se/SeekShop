package com.example.seekshop.network.api

import com.example.seekshop.network.dto.AddressDTO
import com.example.seekshop.network.dto.AuthTokenResponseDTO
import com.example.seekshop.network.dto.GeolocationDTO
import com.example.seekshop.network.dto.ImageDTO
import com.example.seekshop.network.dto.ImageSizeDTO
import com.example.seekshop.network.dto.ItemDTO
import com.example.seekshop.network.dto.LocationDTO
import com.example.seekshop.network.dto.LocationsResponseDTO
import com.example.seekshop.network.dto.PriceDTO
import com.example.seekshop.network.dto.ProductDTO
import com.example.seekshop.network.dto.ProductResponseDTO
import javax.inject.Inject


class FakeRetrofitClient @Inject constructor() : RetrofitClientContract {

    override suspend fun fetchAuthToken(): Result<AuthTokenResponseDTO> {
        return Result.success(
            AuthTokenResponseDTO(
                accessToken = "fake-access-token",
                refreshToken = "fake-refresh-token",
                expiresIn = 3600,
                tokenType = "Bearer"
            )
        )
    }

    override suspend fun fetchLocations(
        authToken: String,
        zipCode: String?,
        latLong: String?
    ): Result<LocationsResponseDTO> {
        return Result.success(
            LocationsResponseDTO(
                data = listOf(
                    LocationDTO(
                        id = "fake-location-id",
                        name = "Fake Kroger",
                        address = AddressDTO(
                            addressLine1 = "123 Test St.",
                            city = "Testville",
                            state = "TS",
                            zipCode = "12345"
                        ),
                        geolocation = GeolocationDTO(
                            latitude = 39.9612,
                            longitude = -82.9988
                        )
                    )
                )
            )
        )
    }

    override suspend fun fetchProduct(
        authToken: String,
        locationId: String,
        term: String
    ): Result<ProductResponseDTO> {
        return Result.success(
            ProductResponseDTO(
                data = listOf(
                    ProductDTO(
                        productId = "fake-product-id",
                        brand = "FakeBrand",
                        description = "Fake Milk",
                        items = listOf(
                            ItemDTO(price = PriceDTO(regular = 3.99, promo = 2.99))
                        ),
                        images = listOf(
                            ImageDTO(
                                sizes = listOf(ImageSizeDTO(url = "https://fake.image.url/milk.png"))
                            )
                        )
                    )
                )
            )
        )
    }
}