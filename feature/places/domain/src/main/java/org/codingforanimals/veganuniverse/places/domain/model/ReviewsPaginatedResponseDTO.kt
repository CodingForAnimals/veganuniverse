package org.codingforanimals.veganuniverse.places.domain.model

data class ReviewsPaginatedResponseDTO(
    val reviews: List<ReviewDomainEntity>,
    val hasMoreItems: Boolean,
)