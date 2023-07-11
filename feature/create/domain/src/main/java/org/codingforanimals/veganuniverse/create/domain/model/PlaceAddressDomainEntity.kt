package org.codingforanimals.veganuniverse.create.domain.model

data class PlaceAddressDomainEntity(
    val streetAddress: String,
    val locality: String,
    val province: String,
    val country: String,
)