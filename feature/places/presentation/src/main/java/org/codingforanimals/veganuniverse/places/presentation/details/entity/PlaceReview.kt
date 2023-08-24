package org.codingforanimals.veganuniverse.places.presentation.details.entity

data class PlaceReview(
    val id: String,
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
    val timestamp: String,
)