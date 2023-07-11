package org.codingforanimals.veganuniverse.places.domain.model

data class ReviewFormDomainEntity(
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
)