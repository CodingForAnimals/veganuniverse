package org.codingforanimals.places.presentation.details.model

import androidx.annotation.StringRes
import org.codingforanimals.places.presentation.details.entity.PlaceReviewViewEntity
import org.codingforanimals.veganuniverse.places.presentation.R

sealed class SubmitReviewStatus {
    object Loading : SubmitReviewStatus()
    data class Success(val placeReviewViewEntity: PlaceReviewViewEntity) : SubmitReviewStatus()
    sealed class Exception(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : SubmitReviewStatus() {
        object GuestUserException : Exception(
            title = R.string.submit_review_error_guest_user_title,
            message = R.string.submit_review_error_guest_user_message,
        )

        object ReviewAlreadyExists : Exception(
            title = R.string.submit_review_error_review_already_exists_title,
            message = R.string.submit_review_error_review_already_exists_message,
        )

        object UnknownException : Exception(
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )
    }
}
