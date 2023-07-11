package org.codingforanimals.places.presentation.model

sealed class GetPlaceReviewsStatus {
    object Loading : GetPlaceReviewsStatus()
    object Exception : GetPlaceReviewsStatus()
    data class Success(val reviews: List<ReviewViewEntity>) : GetPlaceReviewsStatus()
}