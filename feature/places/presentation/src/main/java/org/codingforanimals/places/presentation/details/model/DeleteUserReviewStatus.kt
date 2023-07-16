package org.codingforanimals.places.presentation.details.model

sealed class DeleteUserReviewStatus {
    object Loading : DeleteUserReviewStatus()
    object Error : DeleteUserReviewStatus()
    object Success : DeleteUserReviewStatus()
}
