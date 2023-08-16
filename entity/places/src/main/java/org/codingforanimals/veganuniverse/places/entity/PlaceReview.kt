package org.codingforanimals.veganuniverse.places.entity

data class PlaceReview(
    val id: String,
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
    val timestampSeconds: Long,
)