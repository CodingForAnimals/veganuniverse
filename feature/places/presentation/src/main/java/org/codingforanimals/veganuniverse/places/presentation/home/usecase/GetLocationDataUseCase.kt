package org.codingforanimals.veganuniverse.places.presentation.home.usecase

import android.content.Intent
import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.veganuniverse.places.presentation.home.usecase.model.GetLocationDataStatus
import org.codingforanimals.veganuniverse.services.google.places.api.PlacesClient
import org.codingforanimals.veganuniverse.services.google.places.model.PlaceAutocompleteResult

class GetLocationDataUseCase(
    private val placesClient: PlacesClient,
) {
    suspend operator fun invoke(intent: Intent): GetLocationDataStatus {
        return when (val place = placesClient.getPlaceAutocompleteData(intent)) {
            is PlaceAutocompleteResult.Location -> GetLocationDataStatus.Location(
                LatLng(
                    place.latitude,
                    place.longitude
                )
            )

            else -> GetLocationDataStatus.Error
        }
    }
}

