package org.codingforanimals.veganuniverse.commons.place.shared.model

import java.util.Date

data class PlaceReview(
    val id: String?,
    val userId: String?,
    val username: String?,
    val rating: Int,
    val title: String?,
    val description: String? = null,
    val createdAt: Date?,
)