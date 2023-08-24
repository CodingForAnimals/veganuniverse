package org.codingforanimals.veganuniverse.places.presentation.home.usecase

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.presentation.model.GetPlacesStatus
import org.codingforanimals.veganuniverse.places.presentation.model.toViewEntity

private const val TAG = "GetPlacesUseCase"

internal class GetPlacesUseCase(
    private val placesRepository: PlacesRepository,
) {

    suspend operator fun invoke(
        center: LatLng,
        radiusKm: Double,
    ): Flow<GetPlacesStatus> = flow {
        emit(GetPlacesStatus.Loading)
        try {
            val params = getLocationQueryParams(center, radiusKm)
            val cards = placesRepository.getPlaces(params).mapNotNull { it.toViewEntity() }
            emit(GetPlacesStatus.Success(cards))
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(GetPlacesStatus.Error)
        }
    }

    private fun getLocationQueryParams(
        center: LatLng,
        radiusKm: Double,
    ): GeoLocationQueryParams {
        return GeoLocationQueryParams(
            latitude = center.latitude,
            longitude = center.longitude,
            radiusKm = radiusKm,
        )
    }
}