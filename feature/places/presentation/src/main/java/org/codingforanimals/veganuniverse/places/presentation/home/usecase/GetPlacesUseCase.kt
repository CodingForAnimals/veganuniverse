package org.codingforanimals.veganuniverse.places.presentation.home.usecase

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.presentation.home.entity.PlaceCardViewEntity
import org.codingforanimals.veganuniverse.places.presentation.home.usecase.model.GetPlacesStatus
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
            val adjustedRadiusKm = if (radiusKm >= 5.0) 5.0 else radiusKm
            val params = getLocationQueryParams(center, adjustedRadiusKm)
            val cards = placesRepository.getPlaces(params).mapNotNull {
                PlaceCardViewEntity(
                    card = it.toViewEntity() ?: return@mapNotNull null,
                    markerState = MarkerState(LatLng(it.latitude, it.longitude))
                )
            }
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