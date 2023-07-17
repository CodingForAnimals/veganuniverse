package org.codingforanimals.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.places.presentation.model.GetPlaceReviewsStatus
import org.codingforanimals.places.presentation.model.toViewEntity
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository

private const val TAG = "GetPlaceReviewsUseCase"

class GetPlaceReviewsUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placesRepository: PlacesRepository,
    private val userRepository: UserRepository,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(placeId: String): Flow<GetPlaceReviewsStatus> = flow {
        emit(GetPlaceReviewsStatus.Loading)
        delay(1000)
        val status = try {
            val userId = userRepository.user.value?.id
            val (userReview, paginationResponse) = withContext(ioDispatcher) {
                val userReview = userId?.let { placesRepository.getReview(placeId, it) }
                val paginatedReviews = placesRepository.getReviews(placeId)
                Pair(userReview, paginatedReviews)
            }
            GetPlaceReviewsStatus.Success(
                userReview = userReview?.toViewEntity(),
                paginatedReviews = paginationResponse.reviews
                    .filterNot { it.userId == userId }
                    .mapNotNull { it.toViewEntity() },
                hasMoreReviews = paginationResponse.hasMoreItems,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            GetPlaceReviewsStatus.Exception()
        }
        emit(status)
    }

    suspend fun getMoreReviews(placeId: String): Flow<GetPlaceReviewsStatus> = flow {
        emit(GetPlaceReviewsStatus.Loading)
        delay(1000)
        val status = try {
            val userId = userRepository.user.value?.id
            val paginationResponse = placesRepository.getReviews(placeId)
            GetPlaceReviewsStatus.Success(
                paginatedReviews = paginationResponse.reviews
                    .filterNot { it.userId == userId }
                    .mapNotNull { it.toViewEntity() },
                hasMoreReviews = paginationResponse.hasMoreItems,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            GetPlaceReviewsStatus.Exception()
        }
        emit(status)
    }
}