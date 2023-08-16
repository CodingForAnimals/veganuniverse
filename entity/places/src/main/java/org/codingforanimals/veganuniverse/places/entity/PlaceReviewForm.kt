package org.codingforanimals.veganuniverse.places.entity

data class PlaceReviewForm(
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
)