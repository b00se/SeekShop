package com.example.seekshop.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationsResponseDTO(val data: List<LocationDTO>)

@Serializable
data class LocationDTO(
    @SerialName("locationId")  val id: String,
    val name: String,
    val address: AddressDTO,
    val geolocation: GeolocationDTO,
)

@Serializable
data class AddressDTO(
    val addressLine1: String,
    val city: String,
    val state: String,
    val zipCode: String,
)

@Serializable
data class GeolocationDTO(
    val latitude: Double,
    val longitude: Double,
)
