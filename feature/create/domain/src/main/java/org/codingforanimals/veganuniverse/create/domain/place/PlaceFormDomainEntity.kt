package org.codingforanimals.veganuniverse.create.domain.place

data class PlaceFormDomainEntity(
    val id: String,
    val name: String,
    val openingHours: String,
    val type: String,
    val address: String,
    val city: String,
    val tags: List<String>,
    val geoHash: String,
)