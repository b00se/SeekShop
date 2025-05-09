package com.example.seekshop.network.mappers

import com.example.seekshop.network.dto.AddressDTO
import com.example.seekshop.network.dto.LocationDTO
import com.example.seekshop.domain.model.Address
import com.example.seekshop.domain.model.Location

fun LocationDTO.toDomain() = Location(
    id = id,
    name = name,
    address = address.toDomain(),
    lat = geolocation.latitude,
    lon = geolocation.longitude,
)

fun AddressDTO.toDomain() = Address(
    street = addressLine1,
    city = city,
    state = state,
    zip = zipCode,
)