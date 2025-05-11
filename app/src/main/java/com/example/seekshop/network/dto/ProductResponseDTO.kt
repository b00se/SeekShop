package com.example.seekshop.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseDTO(
    val data: List<ProductDTO>
)
