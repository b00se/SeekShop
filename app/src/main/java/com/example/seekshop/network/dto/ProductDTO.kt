package com.example.seekshop.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    val productId: String,
    val brand: String,
    val description: String,
    val items: List<ItemDTO>,
    val images: List<ImageDTO>,
)

@Serializable
data class ItemDTO(
    val price: PriceDTO? = null,
    val size: String? = null,
)

@Serializable
data class PriceDTO(
    val regular: Double,
    val promo: Double
)

@Serializable
data class ImageDTO(
    val sizes: List<ImageSizeDTO>? = null
)

@Serializable
data class ImageSizeDTO(
    val url: String
)