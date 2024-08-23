package org.codingforanimals.veganuniverse.place.home

import android.content.Intent
import org.codingforanimals.veganuniverse.services.google.places.api.PlacesClient
import org.codingforanimals.veganuniverse.services.google.places.model.PlaceAutocompleteResult

class GetPlaceLocationData(
    private val placesClient: PlacesClient,
) {
    suspend operator fun invoke(intent: Intent): Result {
        return when (val place = placesClient.getPlaceAutocompleteData(intent)) {
            is PlaceAutocompleteResult.Location -> Result.Success(place.latitude, place.longitude)
            else -> Result.Error
        }
    }

    sealed class Result {
        data object Error : Result()
        data class Success(val latitude: Double, val longitude: Double) : Result()
    }
}