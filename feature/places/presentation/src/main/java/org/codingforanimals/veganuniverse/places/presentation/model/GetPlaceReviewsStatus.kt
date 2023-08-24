package org.codingforanimals.veganuniverse.places.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.places.presentation.R
import org.codingforanimals.veganuniverse.places.presentation.details.entity.PlaceReview

sealed class GetPlaceReviewsStatus {
    object Loading : GetPlaceReviewsStatus()
    data class Exception(
        @StringRes val error: Int = R.string.error_unknown_failure_message,
    ) : GetPlaceReviewsStatus()

    data class Success(
        val userReview: PlaceReview? = null,
        val paginatedReviews: List<PlaceReview>,
        val hasMoreReviews: Boolean,
    ) : GetPlaceReviewsStatus()
}

sealed class GetUserReviewStatus {
    object Loading : GetUserReviewStatus()
    data class Exception(
        @StringRes val message: Int = R.string.place_details_get_user_review_error_message,
    ) : GetUserReviewStatus()

    data class Success(
        val userReview: PlaceReview?,
    ) : GetUserReviewStatus()
}