package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.presentation.model.GetPlaceReviewsStatus
import org.codingforanimals.veganuniverse.places.presentation.model.GetUserReviewStatus
import org.codingforanimals.veganuniverse.places.presentation.model.toViewEntity

private const val TAG = "GetPlaceReviewsUseCase"

internal class GetPlaceReviewsUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placesRepository: PlacesRepository,
    private val userRepository: UserRepository,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(placeId: String): Flow<GetPlaceReviewsStatus> = flow {
        emit(GetPlaceReviewsStatus.Loading)
        val status = try {
            val userId = userRepository.user.firstOrNull()?.id
            val (userReview, paginationResponse) = withContext(ioDispatcher) {
                val userReview = userId?.let { placesRepository.getReview(placeId, it) }
                val paginatedReviews = placesRepository.getReviews(placeId)
                Pair(userReview, paginatedReviews)
            }
            GetPlaceReviewsStatus.Success(
                userReview = userReview?.toViewEntity(),
                paginatedReviews = paginationResponse.content
                    .filterNot { it.userId == userId }
                    .map { it.toViewEntity() },
                hasMoreReviews = paginationResponse.hasMoreItems,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            GetPlaceReviewsStatus.Exception()
        }
        emit(status)
    }

    suspend fun getUserReview(placeId: String): Flow<GetUserReviewStatus> = flow {
        emit(GetUserReviewStatus.Loading)
        val status = try {
            val userReview = userRepository.user.firstOrNull()?.id?.let { userId ->
                withContext(ioDispatcher) { placesRepository.getReview(placeId, userId) }
            }
            GetUserReviewStatus.Success(userReview = userReview?.toViewEntity())
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            GetUserReviewStatus.Exception()
        }
        emit(status)
    }

    suspend fun getMoreReviews(placeId: String): Flow<GetPlaceReviewsStatus> = flow {
        emit(GetPlaceReviewsStatus.Loading)
        val status = try {
            val userId = userRepository.user.firstOrNull()?.id
            val paginationResponse = placesRepository.getReviews(placeId)
            GetPlaceReviewsStatus.Success(
                paginatedReviews = paginationResponse.content
                    .filterNot { it.userId == userId }
                    .map { it.toViewEntity() },
                hasMoreReviews = paginationResponse.hasMoreItems,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            GetPlaceReviewsStatus.Exception()
        }
        emit(status)
    }
}