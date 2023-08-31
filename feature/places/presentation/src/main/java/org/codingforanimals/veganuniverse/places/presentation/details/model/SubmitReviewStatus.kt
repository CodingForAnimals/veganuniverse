package org.codingforanimals.veganuniverse.places.presentation.details.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.places.presentation.R
import org.codingforanimals.veganuniverse.places.presentation.details.entity.PlaceReview

sealed class SubmitReviewStatus {
    data object Loading : SubmitReviewStatus()
    data class Success(val placeReview: PlaceReview) : SubmitReviewStatus()
    sealed class Exception(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : SubmitReviewStatus() {
        data object GuestUserException : Exception(
            title = R.string.submit_review_error_guest_user_title,
            message = R.string.submit_review_error_guest_user_message,
        )

        data object ReviewAlreadyExists : Exception(
            title = R.string.submit_review_error_review_already_exists_title,
            message = R.string.submit_review_error_review_already_exists_message,
        )

        data object UnknownException : Exception(
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )
    }
}
