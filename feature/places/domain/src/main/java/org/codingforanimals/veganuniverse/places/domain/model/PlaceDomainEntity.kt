package org.codingforanimals.veganuniverse.places.domain.model

data class PlaceDomainEntity(
    val id: String,
    val imageRef: String,
    val type: String,
    val description: String,
    val name: String,
    val rating: Int,
    val reviewCount: Int,
    val address: String,
    val city: String,
    val tags: List<String>,
    val latitude: Double,
    val longitude: Double,
    val geoHash: String,
    val timestamp: Long,
    val openingHours: String,
)