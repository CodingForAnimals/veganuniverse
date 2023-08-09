package org.codingforanimals.veganuniverse.create.domain.model

import org.codingforanimals.veganuniverse.shared.entity.places.OpeningHours

data class PlaceFormDomainEntity(
    val name: String,
    val addressComponents: PlaceAddressDomainEntity,
    val description: String,
    val openingHours: List<OpeningHours>,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val geoHash: String,
    val tags: List<String>,
)
