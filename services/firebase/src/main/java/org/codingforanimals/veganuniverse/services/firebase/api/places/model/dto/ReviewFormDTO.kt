package org.codingforanimals.veganuniverse.services.firebase.api.places.model.dto

data class ReviewFormDTO(
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
)