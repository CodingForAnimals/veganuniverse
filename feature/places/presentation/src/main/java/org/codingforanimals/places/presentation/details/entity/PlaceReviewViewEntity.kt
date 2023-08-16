package org.codingforanimals.places.presentation.details.entity

data class PlaceReviewViewEntity(
    val id: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
    val timestamp: String,
)