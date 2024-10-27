package org.codingforanimals.veganuniverse.place.shared.model

sealed class PlaceReviewUserFilter(open val userId: String) {
    data class ExcludeUser(override val userId: String) : PlaceReviewUserFilter(userId)
    data class FilterByUser(override val userId: String) : PlaceReviewUserFilter(userId)
}