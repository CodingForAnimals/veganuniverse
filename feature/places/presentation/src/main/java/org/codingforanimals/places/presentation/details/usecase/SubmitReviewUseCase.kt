package org.codingforanimals.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.places.presentation.details.model.SubmitReviewStatus
import org.codingforanimals.places.presentation.model.toViewEntity
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.domain.model.ReviewFormDomainEntity
import org.codingforanimals.veganuniverse.user.UserRepository
import org.codingforanimals.veganuniverse.user.model.User

private const val TAG = "SubmitReviewUseCase"

class SubmitReviewUseCase(
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
            val review = getReviewDomainEntity(rating, title, description, user)
            val result = try {
                val existingUserReview = placesRepository.getReview(placeId, user.id)
                if (existingUserReview == null) {
                    val uploadedReview =
                        placesRepository.submitReview(placeId, review).toViewEntity()
                    SubmitReviewStatus.Success(uploadedReview)
                } else {
                    SubmitReviewStatus.Exception.ReviewAlreadyExists
                }
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                SubmitReviewStatus.Exception.UnknownException
            }
            emit(result)
        }

    private fun getReviewDomainEntity(
        rating: Int,
        title: String,
        description: String?,
        user: User,
    ): ReviewFormDomainEntity {
        return ReviewFormDomainEntity(
            userId = user.id,
            username = user.name,
            rating = rating,
            title = title,
            description = description?.ifBlank { null },
        )
    }
}