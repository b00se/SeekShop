package com.example.seekshop.domain.model

data class Product(
    val id: String,
    val brand: String,
    val description: String,
    val imageUrl: String?,
    val price: Price
)

data class Price( val regular: Double, val promo: Double )
