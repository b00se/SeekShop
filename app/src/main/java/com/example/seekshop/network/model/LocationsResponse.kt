package com.example.seekshop.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationsResponse(val data: List<Location>)

@Serializable
data class Location(
    @SerialName("locationId")  val id: String,
    val name: String,
    val address: Address,
    val geolocation: Geolocation,
)

@Serializable
data class Address(
    val addressLine1: String,
    val city: String,
    val state: String,
    val zipCode: String,
)

@Serializable
data class Geolocation(
    val latitude: Double,
    val longitude: Double,
)
