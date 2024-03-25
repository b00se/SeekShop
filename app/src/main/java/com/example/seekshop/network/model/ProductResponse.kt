package com.example.seekshop.network.model

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class ProductResponse(
    val data: List<Product>,
)

@Serializable(with = ProductSerializer::class)
data class Product(
    @SerialName("productId") val id: String,
    val brand: String,
    val description: String,
    val images: List<Image>,
    val price: Price
)

@Serializable
data class Image( val urls: List<String>)

@Serializable
data class Price( val regular: Double, val promo: Double )

object ProductSerializer : KSerializer<Product> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Product") {
        element<String>("productId")
        element<String>("brand")
        element<String>("description")
        element("images", Image.serializer().descriptor)
        element("price", Price.serializer().descriptor)
    }

    override fun serialize(encoder: Encoder, value: Product) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): Product {
        val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("This class can be loaded only by Json")
        val element = jsonDecoder.decodeJsonElement().jsonObject

        val id = element["productId"]?.jsonPrimitive?.contentOrNull ?: ""
        val brand = element["brand"]?.jsonPrimitive?.contentOrNull ?: ""
        val description = element["description"]?.jsonPrimitive?.contentOrNull ?: ""

        // Extract image Urls
        val images = element["images"]?.jsonArray?.flatMap { imageElement ->
            imageElement.jsonObject["sizes"]?.jsonArray?.mapNotNull { sizeElement ->
                sizeElement.jsonObject["url"]?.jsonPrimitive?.contentOrNull
            } ?: emptyList()
        }?.let { urls -> listOf(Image(urls)) } ?: listOf()

        // Extract price
        val firstItemPrice = element["items"]?.jsonArray?.firstOrNull()?.jsonObject?.get("price")?.jsonObject
        val regular = firstItemPrice?.get("regular")?.jsonPrimitive?.doubleOrNull ?: 0.0
        val promo = firstItemPrice?.get("promo")?.jsonPrimitive?.doubleOrNull ?: 0.0

        val price = Price(regular, promo)

        return Product(id, brand, description, images, price)
    }

}
