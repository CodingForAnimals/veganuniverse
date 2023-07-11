package org.codingforanimals.veganuniverse.services.firebase.places.model.dto

data class ReviewFormDTO(
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
)