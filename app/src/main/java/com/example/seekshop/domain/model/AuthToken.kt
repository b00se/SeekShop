package com.example.seekshop.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Int,
    val tokenType: String,
)
