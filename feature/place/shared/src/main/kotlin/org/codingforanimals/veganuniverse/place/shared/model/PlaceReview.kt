package org.codingforanimals.veganuniverse.place.shared.model

import java.util.Date

data class PlaceReview(
    val id: String?,
    val userId: String?,
    val username: String?,
    val placeName: String?,
    val rating: Int,
    val title: String?,
    val description: String? = null,
    val createdAt: Date?,
)