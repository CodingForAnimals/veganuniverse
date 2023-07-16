package org.codingforanimals.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.delay
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
            delay(2000)
            val user = userRepository.user.value
                ?: return@flow emit(SubmitReviewStatus.Exception.GuestUserException)
            val form = getReviewDomainEntity(rating, title, description, user)
            try {
                val existingUserReview = placesRepository.getReview(placeId, user.id)
                if (existingUserReview != null) {
                    return@flow emit(SubmitReviewStatus.Exception.ReviewAlreadyExists)
                }
                val uploadedReview = placesRepository.submitReview(placeId, form).toViewEntity()!!
                emit(SubmitReviewStatus.Success(uploadedReview))
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                emit(SubmitReviewStatus.Exception.UnknownException)
            }
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