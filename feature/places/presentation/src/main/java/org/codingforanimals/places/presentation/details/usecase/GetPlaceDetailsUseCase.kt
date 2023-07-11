package org.codingforanimals.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.places.presentation.model.GetPlaceDetailsStatus
import org.codingforanimals.places.presentation.model.toViewEntity
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.user.UserRepository

private const val TAG = "GetPlaceDetailsUseCase"

class GetPlaceDetailsUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placesRepository: PlacesRepository,
    private val userRepository: UserRepository,
) {
    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(placeId: String): Flow<GetPlaceDetailsStatus> =
        flow {
            emit(GetPlaceDetailsStatus.Loading)
            val status = try {
                val (place, review) = withContext(ioDispatcher) {
                    val place = placesRepository.getPlace(placeId).toViewEntity()!!
                    val userReview = userRepository.user.value?.id?.let { userId ->
                        placesRepository.getReview(placeId, userId)?.toViewEntity()
                    }
                    Pair(place, userReview)
                }
                GetPlaceDetailsStatus.Success(place, review)
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                GetPlaceDetailsStatus.Exception.UnknownException
            }
            emit(status)
        }
}