package org.codingforanimals.places.presentation.model

data class ReviewViewEntity(
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
    val timestamp: String,
)