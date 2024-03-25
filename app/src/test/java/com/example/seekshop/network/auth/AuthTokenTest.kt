package com.example.seekshop.network.auth

import com.example.seekshop.network.RetrofitClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthTokenTest {

    @Test
    fun testFetchAuthToken(): Unit = runBlocking {
        val result = RetrofitClient.fetchAuthToken()

        assertThat(result.isSuccess).isTrue()
        result.onSuccess { authTokenResponse ->
            assertThat(authTokenResponse.accessToken).isNotEmpty()
            assertThat(authTokenResponse.expiresIn).isGreaterThan(0)
            assertThat(authTokenResponse.tokenType).isEqualTo("bearer")}
    }
}