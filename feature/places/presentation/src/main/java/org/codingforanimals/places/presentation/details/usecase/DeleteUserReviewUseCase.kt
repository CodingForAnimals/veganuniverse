package org.codingforanimals.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.places.presentation.details.model.DeleteUserReviewStatus
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository

private const val TAG = "DeleteUserReviewUseCase"

class DeleteUserReviewUseCase(
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(placeId: String, reviewId: String): Flow<DeleteUserReviewStatus> =
        flow {
            emit(DeleteUserReviewStatus.Loading)
            try {
                placesRepository.deleteReview(placeId, reviewId)
                emit(DeleteUserReviewStatus.Success)
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                emit(DeleteUserReviewStatus.Error)
            }
        }
}