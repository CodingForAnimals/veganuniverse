package org.codingforanimals.veganuniverse.places.entity

data class PaginatedResponse<T>(
    val content: List<T>,
    val hasMoreItems: Boolean,
)
//
//data class ReviewsPaginatedResponse(
//    val reviews: List<PlaceReview>,
//    val hasMoreItems: Boolean,
//)