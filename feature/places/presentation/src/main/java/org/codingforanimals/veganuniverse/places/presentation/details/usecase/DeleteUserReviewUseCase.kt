package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.presentation.details.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.presentation.details.entity.toDomainEntity
import org.codingforanimals.veganuniverse.places.presentation.details.model.DeleteUserReviewStatus

private const val TAG = "DeleteUserReviewUseCase"

class DeleteUserReviewUseCase(
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(
        placeId: String,
        review: PlaceReview,
    ): Flow<DeleteUserReviewStatus> =
        flow {
            emit(DeleteUserReviewStatus.Loading)
            try {
                placesRepository.deleteReview(placeId, review.toDomainEntity())
                emit(DeleteUserReviewStatus.Success)
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                emit(DeleteUserReviewStatus.Error)
            }
        }
}