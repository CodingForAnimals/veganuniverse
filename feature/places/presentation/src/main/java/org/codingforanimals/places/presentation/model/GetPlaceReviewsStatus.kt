package org.codingforanimals.places.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.places.presentation.R

sealed class GetPlaceReviewsStatus {
    object Loading : GetPlaceReviewsStatus()
    data class Exception(
        @StringRes val error: Int = R.string.error_unknown_failure_message,
    ) : GetPlaceReviewsStatus()

    data class Success(
        val userReview: ReviewViewEntity? = null,
        val paginatedReviews: List<ReviewViewEntity>,
        val hasMoreReviews: Boolean,
    ) : GetPlaceReviewsStatus()
}