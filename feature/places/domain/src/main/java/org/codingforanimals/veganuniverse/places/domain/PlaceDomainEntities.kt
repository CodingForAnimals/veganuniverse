package org.codingforanimals.veganuniverse.places.domain

data class PlaceDomainEntity(
    val id: String = "",
    val imageRef: String = "",
    val type: String = "",
    val name: String = "",
    val rating: Int = -1,
    val reviewCount: Int = 0,
    val address: String = "",
    val city: String = "",
    val tags: List<String> = emptyList(),
    val timestamp: Long = 0,
)

data class PlaceQueryBound(
    val startHash: String,
    val endHash: String,
)