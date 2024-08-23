package org.codingforanimals.veganuniverse.create.place.presentation.usecase

import android.content.Intent
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import kotlin.math.sqrt
import org.codingforanimals.veganuniverse.services.location.UserLocationManager
import org.codingforanimals.veganuniverse.services.location.model.LocationResponse
import org.codingforanimals.veganuniverse.services.google.places.api.AutocompleteIntentParams
import org.codingforanimals.veganuniverse.services.google.places.api.PlacesClient


class GetAutoCompleteIntentUseCase(
    private val placesClient: PlacesClient,
    private val userLocationManager: UserLocationManager,
) {
    operator fun invoke(): Intent {
        val locationBiasBounds = when (val userLocation = userLocationManager.userLocation.value) {
            is LocationResponse.LocationGranted -> {
                val center = LatLng(
                    userLocation.latitude,
                    userLocation.longitude
                )
                val targetNorthEast = SphericalUtil
                    .computeOffset(center, LOCATION_BIAS_RADIUS * sqrt(2.0), 45.0)
                val targetSouthWest = SphericalUtil
                    .computeOffset(center, LOCATION_BIAS_RADIUS * sqrt(2.0), 225.0)
                LatLngBounds
                    .builder()
                    .include(targetNorthEast)
                    .include(targetSouthWest)
                    .build()
            }

            else -> null
        }
        return placesClient.getPlaceAutocompleteIntent(
            AutocompleteIntentParams(locationBiasBounds = locationBiasBounds)
        )
    }

    private companion object {
        const val LOCATION_BIAS_RADIUS = 5
    }
}