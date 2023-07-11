package org.codingforanimals.veganuniverse.create.domain.model

data class PlaceFormDomainEntity(
    val name: String,
    val addressComponents: PlaceAddressDomainEntity,
    val description: String,
    val openingHours: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val geoHash: String,
    val tags: List<String>,
)

