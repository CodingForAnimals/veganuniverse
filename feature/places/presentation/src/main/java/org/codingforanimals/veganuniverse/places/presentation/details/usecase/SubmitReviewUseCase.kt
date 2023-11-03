package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.presentation.details.model.SubmitReviewStatus
import org.codingforanimals.veganuniverse.places.presentation.model.toViewEntity

private const val TAG = "SubmitReviewUseCase"

internal class SubmitReviewUseCase(
    private val placesRepository: PlacesRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        placeId: String,
        rating: Int,
        title: String,
        description: String?,
    ): Flow<SubmitReviewStatus> =
        flow {
            emit(SubmitReviewStatus.Loading)
            val user = userRepository.user.value
                ?: return@flow emit(SubmitReviewStatus.Exception.GuestUserException)
            val form = PlaceReviewForm(
                userId = user.id,
                username = user.name,
                rating = rating,
                title = title,
                description = description?.ifBlank { null }
            )
            try {
                val existingUserReview = placesRepository.getReview(placeId, user.id)
                if (existingUserReview != null) {
                    return@flow emit(SubmitReviewStatus.Exception.ReviewAlreadyExists)
                }
                val uploadedReview = placesRepository.submitReview(placeId, form).toViewEntity()
                emit(SubmitReviewStatus.Success(uploadedReview))
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                emit(SubmitReviewStatus.Exception.UnknownException)
            }
        }
}