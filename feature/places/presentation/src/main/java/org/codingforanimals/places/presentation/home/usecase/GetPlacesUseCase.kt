package org.codingforanimals.places.presentation.home.usecase

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.places.presentation.home.PlaceViewEntity
import org.codingforanimals.places.presentation.home.toViewEntity
import org.codingforanimals.veganuniverse.places.domain.PlaceQueryBound
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository

class GetPlacesUseCase(
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(
        center: LatLng,
        radiusInMeters: Double
    ): Result<List<PlaceViewEntity>> = runCatching {
        val bounds = getBounds(center, radiusInMeters)
        placesRepository.getPlaces(bounds).toViewEntity()
    }

    private fun getBounds(center: LatLng, radiusInMeters: Double) = GeoFireUtils
        .getGeoHashQueryBounds(center.toGeoLocation(), radiusInMeters)
        .map { PlaceQueryBound(it.startHash, it.endHash) }

    private fun LatLng.toGeoLocation() = GeoLocation(latitude, longitude)
}