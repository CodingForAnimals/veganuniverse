package org.codingforanimals.veganuniverse.services.firebase.places.model

data class ReviewsPaginatedResponse(
    val reviews: List<ReviewFirebaseEntity>,
    val hasMoreItems: Boolean,
)