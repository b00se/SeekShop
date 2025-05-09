package com.example.seekshop.network.auth

import com.example.seekshop.network.api.RetrofitClient
import com.example.seekshop.network.mappers.toDomain
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FetchProductTest {

    private var authToken: String = ""
    private var locationId: String = ""
    private val client = RetrofitClient()

    @Before
    fun setUp(): Unit = runBlocking {
        authToken = client.fetchAuthToken().getOrThrow().accessToken
        locationId = client.fetchLocations(
            authToken = authToken,
            zipCode = "43210",
        ).getOrThrow().data.first().id
    }

    @Test
    fun testFetchProduct(): Unit = runBlocking {
        val result = client.fetchProduct(
            authToken = authToken,
            locationId = locationId,
            term = "milk"
        )

        assertWithMessage("Expected success was failure with exception: ${result.exceptionOrNull()}")
            .that(result.isSuccess).isTrue()
        result.onSuccess { productResponse ->
            assertThat(productResponse.data).isNotEmpty()
            with(productResponse.data.first().toDomain()) {
                assertThat(id).isNotEmpty()
                assertThat(imageUrl).isNotEmpty()
                assertThat(description).isNotEmpty()
                assertThat(price.regular).isAtLeast(0)
                assertThat(price.promo).isAtLeast(0)
            }
        }
    }

}