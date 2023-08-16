package org.codingforanimals.places.presentation.home.usecase

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.places.presentation.model.GetPlacesStatus
import org.codingforanimals.places.presentation.model.toViewEntity
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams

private const val TAG = "GetPlacesUseCase"

internal class GetPlacesUseCase(
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(
        center: LatLng,
        radiusInMeters: Double,
    ): Flow<GetPlacesStatus> = flow {
        emit(GetPlacesStatus.Loading)
        try {
            val params = getLocationQueryParams(center, radiusInMeters)
            val places = placesRepository.getPlaces(params).mapNotNull { it.toViewEntity() }
            emit(GetPlacesStatus.Success(places))
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(GetPlacesStatus.Error)
        }
    }

    private fun getLocationQueryParams(
        center: LatLng,
        radiusInMeters: Double,
    ): GeoLocationQueryParams {
        return GeoLocationQueryParams(
            latitude = center.latitude,
            longitude = center.longitude,
            radiusInMeters = radiusInMeters,
        )
    }
}