package org.codingforanimals.veganuniverse.places.presentation.details.model

sealed class DeleteUserReviewStatus {
    data object Loading : DeleteUserReviewStatus()
    data object Error : DeleteUserReviewStatus()
    data object Success : DeleteUserReviewStatus()
}
