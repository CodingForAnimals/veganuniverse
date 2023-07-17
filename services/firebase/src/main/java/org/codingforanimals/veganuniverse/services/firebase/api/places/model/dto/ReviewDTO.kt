package org.codingforanimals.veganuniverse.services.firebase.api.places.model.dto

data class ReviewDTO(
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
    val timestampSeconds: Long,
)