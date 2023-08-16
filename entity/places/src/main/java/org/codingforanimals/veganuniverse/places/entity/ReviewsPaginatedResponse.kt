package org.codingforanimals.veganuniverse.places.entity

data class ReviewsPaginatedResponse(
    val reviews: List<PlaceReview>,
    val hasMoreItems: Boolean,
)