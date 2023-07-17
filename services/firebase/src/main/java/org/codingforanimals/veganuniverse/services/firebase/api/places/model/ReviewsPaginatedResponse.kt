package org.codingforanimals.veganuniverse.services.firebase.api.places.model

data class ReviewsPaginatedResponse(
    val reviews: List<ReviewFirebaseEntity>,
    val hasMoreItems: Boolean,
)