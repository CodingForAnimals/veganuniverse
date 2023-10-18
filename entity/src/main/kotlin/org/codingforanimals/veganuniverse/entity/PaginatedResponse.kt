package org.codingforanimals.veganuniverse.entity

data class PaginatedResponse<T>(
    val content: List<T>,
    val hasMoreItems: Boolean,
)