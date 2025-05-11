package com.example.seekshop.domain.model

data class Location(
    val id: String,
    val name: String,
    val address: Address,
    val lat: Double,
    val lon: Double,
)

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zip: String,
)