package com.example.seekshop.network.mappers

import com.example.seekshop.network.dto.PriceDTO
import com.example.seekshop.network.dto.ProductDTO
import com.example.seekshop.domain.model.Price
import com.example.seekshop.domain.model.Product

fun ProductDTO.toDomain(): Product {
    val imageUrl = images
        .flatMap { it.sizes.orEmpty() }
        .firstOrNull()?.url

    val price = items.firstOrNull()?.price ?: PriceDTO(0.0, 0.0)

    return Product(
        id = productId,
        brand = brand,
        description = description,
        imageUrl = imageUrl,
        price = Price(price.regular, price.promo)
    )
}