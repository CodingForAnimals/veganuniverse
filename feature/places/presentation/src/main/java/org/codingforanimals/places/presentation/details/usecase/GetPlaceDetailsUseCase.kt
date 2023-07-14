package org.codingforanimals.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.places.presentation.model.GetPlaceDetailsStatus
import org.codingforanimals.places.presentation.model.toViewEntity
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository

private const val TAG = "GetPlaceDetailsUseCase"

class GetPlaceDetailsUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placesRepository: PlacesRepository,
) {
    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(placeId: String): Flow<GetPlaceDetailsStatus> =
        flow {
            emit(GetPlaceDetailsStatus.Loading)
            val status = try {
                val place = withContext(ioDispatcher) {
                    placesRepository.getPlace(placeId).toViewEntity()!!
                }
                GetPlaceDetailsStatus.Success(place)
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                GetPlaceDetailsStatus.Exception.UnknownException
            }
            emit(status)
        }
}