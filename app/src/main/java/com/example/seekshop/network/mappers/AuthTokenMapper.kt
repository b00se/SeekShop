package com.example.seekshop.network.mappers

import com.example.seekshop.domain.model.AuthToken
import com.example.seekshop.network.dto.AuthTokenResponseDTO

fun AuthTokenResponseDTO.toDomain(): AuthToken {
    return AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresIn = expiresIn,
        tokenType = tokenType,
    )
}