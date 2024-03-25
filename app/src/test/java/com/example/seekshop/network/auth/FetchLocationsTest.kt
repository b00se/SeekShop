package com.example.seekshop.network.auth

import com.example.seekshop.network.RetrofitClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FetchLocationsTest {

    private var authToken: String = ""

    @Before
    fun setUp(): Unit = runBlocking {
        authToken = RetrofitClient.fetchAuthToken().getOrThrow().accessToken
    }

    @Test
    fun testFetchLocations(): Unit = runBlocking {
        val result = RetrofitClient.fetchLocations(
            authToken = authToken,
            zipCode = "43210",
            )

        assertThat(result.isSuccess).isTrue()
        result.onSuccess { locationsResponse ->
            assertThat(locationsResponse.data).isNotEmpty()
            assertThat(locationsResponse.data).hasSize(10)
            assertThat(locationsResponse.data.first().id).isNotEmpty()
        }
    }
}