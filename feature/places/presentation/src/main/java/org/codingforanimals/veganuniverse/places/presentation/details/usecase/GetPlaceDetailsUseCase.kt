package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.presentation.model.GetPlaceDetailsStatus
import org.codingforanimals.veganuniverse.places.presentation.model.toViewEntity

private const val TAG = "GetPlaceDetailsUseCase"

internal class GetPlaceDetailsUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placesRepository: PlacesRepository,
) {
    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(placeId: String): Flow<GetPlaceDetailsStatus> =
        flow {
            emit(GetPlaceDetailsStatus.Loading)
            val status = try {
                val place = withContext(ioDispatcher) {
                    placesRepository.getPlace(placeId)?.toViewEntity()!!
                }
                GetPlaceDetailsStatus.Success(place)
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                GetPlaceDetailsStatus.Exception.UnknownException
            }
            emit(status)
        }
}