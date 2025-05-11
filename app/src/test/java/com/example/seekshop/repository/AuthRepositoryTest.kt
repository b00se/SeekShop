package com.example.seekshop.repository

import com.example.seekshop.network.api.FakeRetrofitClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var fakeStorage: FakeSecureTokenStorage
    private lateinit var fakeClient: FakeRetrofitClient

    @Before
    fun setup() {
        fakeStorage = FakeSecureTokenStorage()
        fakeClient = FakeRetrofitClient()

        authRepository = AuthRepository(
            secureTokenStorage = fakeStorage,
            authServe = fakeClient
        )
    }

    @Test
    fun `fetches new token when none exists`() = runBlocking {
        val token = authRepository.getAuthToken()
        assertThat(token).isEqualTo("fake-access-token")
    }

    @Test
    fun `uses cached token if valid`() = runBlocking {
        fakeStorage.saveTokenWithExpiration(
            token = "cached-token",
            expiration = System.currentTimeMillis() + 60_000L
        )

        val token = authRepository.getAuthToken()
        assertThat(token).isEqualTo("cached-token")
    }

    @Test
    fun `refreshes token if expired`() = runBlocking {
        fakeStorage.saveTokenWithExpiration(
            token = "expired-token",
            expiration = System.currentTimeMillis() - 10_000L
        )

        val token = authRepository.getAuthToken()
        assertThat(token).isEqualTo("fake-access-token")
    }
}