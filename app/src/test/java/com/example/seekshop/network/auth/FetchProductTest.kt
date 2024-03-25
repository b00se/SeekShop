package com.example.seekshop.network.auth

import com.example.seekshop.network.RetrofitClient
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

    @Before
    fun setUp(): Unit = runBlocking {
        authToken = RetrofitClient.fetchAuthToken().getOrThrow().accessToken
        locationId = RetrofitClient.fetchLocations(
            authToken = authToken,
            zipCode = "43210",
        ).getOrThrow().data.first().id
    }

    @Test
    fun testFetchProduct(): Unit = runBlocking {
        val result = RetrofitClient.fetchProduct(
            authToken = authToken,
            locationId = locationId,
            term = "milk"
        )

        assertWithMessage("Expected success was failure with exception: ${result.exceptionOrNull()}")
            .that(result.isSuccess).isTrue()
        result.onSuccess { productResponse ->
            assertThat(productResponse.data).isNotEmpty()
            with(productResponse.data.first()) {
                assertThat(id).isNotEmpty()
                assertThat(images).isNotEmpty()
                assertThat(description).isNotEmpty()
                assertThat(price.regular).isGreaterThan(0)
                assertThat(price.promo).isAtLeast(0)
            }
        }
    }

}