package org.codingforanimals.places.presentation.home.usecase

import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.places.presentation.model.PlaceViewEntity
import org.codingforanimals.places.presentation.model.toViewEntity
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.domain.model.PlaceLocationQueryParams

class GetPlacesUseCase(
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(
        center: LatLng,
        radiusInMeters: Double,
    ): Result<List<PlaceViewEntity>> = runCatching {
        val params = getLocationQueryParams(center, radiusInMeters)
        placesRepository.getPlaces(params).mapNotNull { it.toViewEntity() }
    }

    private fun getLocationQueryParams(
        center: LatLng,
        radiusInMeters: Double,
    ): PlaceLocationQueryParams {
        return PlaceLocationQueryParams(
            latitude = center.latitude,
            longitude = center.longitude,
            radiusInMeters = radiusInMeters,
        )
    }
}