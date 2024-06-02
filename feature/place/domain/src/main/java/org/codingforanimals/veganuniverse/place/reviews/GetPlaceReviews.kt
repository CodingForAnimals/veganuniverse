package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.codingforanimals.veganuniverse.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.place.model.PlaceReview
import org.codingforanimals.veganuniverse.place.model.PlaceReviewQueryParams
import org.codingforanimals.veganuniverse.place.model.PlaceReviewUserFilter
import org.codingforanimals.veganuniverse.user.domain.model.User
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser

private const val TAG = "GetPlaceReviews"

class GetPlaceReviews(
    private val getCurrentUser: GetCurrentUser,
    private val placeReviewRepository: PlaceReviewRepository,
) {
    suspend operator fun invoke(placeId: String): GetReviewsResult = coroutineScope {
        val user = runCatching { getCurrentUser() }
            .onFailure { Log.e(TAG, it.stackTraceToString()) }

        val userReviewDeferred = async {
            runCatching {
                user.getOrNull()?.id?.let { userId ->
                    val userReviewQueryParams = PlaceReviewQueryParams.Builder(placeId)
                        .withUserFilter(PlaceReviewUserFilter.FilterByUser(userId)).withPageSize(1)
                        .withMaxSize(1)
                        .build()
                    val userReviews =
                        placeReviewRepository.queryPlaceReviews(userReviewQueryParams)
                    userReviews.firstOrNull()
                }
            }.onFailure { Log.e(TAG, it.stackTraceToString()) }
        }

        val otherReviewsDeferred = async {
            var otherReviewsQueryParams = PlaceReviewQueryParams.Builder(placeId)
                .withMaxSize(2).withPageSize(2)
            user.getOrNull()?.id?.let { userId ->
                otherReviewsQueryParams = otherReviewsQueryParams
                    .withUserFilter(PlaceReviewUserFilter.ExcludeUser(userId))
            }
            runCatching {
                placeReviewRepository.queryPlaceReviews(otherReviewsQueryParams.build())
            }.onFailure { Log.e(TAG, it.stackTraceToString()) }
        }

        val userReview = userReviewDeferred.await()
        val otherReviews = otherReviewsDeferred.await()
        GetReviewsResult(
            user = user,
            userReview = userReview,
            otherReviews = otherReviews
        )
    }

    data class GetReviewsResult(
        val user: Result<User?>,
        val userReview: Result<PlaceReview?>,
        val otherReviews: Result<List<PlaceReview>>,
    )


}