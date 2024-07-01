package org.codingforanimals.veganuniverse.create.place.presentation.usecase

import android.content.Intent
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.create.place.presentation.model.GetPlaceDataStatus
import org.codingforanimals.veganuniverse.services.google.places.model.PlaceAutocompleteResult

class GetPlaceDataUseCase(
    private val placesClient: org.codingforanimals.veganuniverse.services.google.places.api.PlacesClient,
) {

    operator fun invoke(intent: Intent): Flow<GetPlaceDataStatus> = flow {
        emit(GetPlaceDataStatus.Loading)
        val place = placesClient.getPlaceAutocompleteData(intent)
        emit(place.toStatus())
    }

    private fun PlaceAutocompleteResult.toStatus(): GetPlaceDataStatus {
        return when (this) {
            is PlaceAutocompleteResult.Establishment -> GetPlaceDataStatus.EstablishmentData(
                latLng = LatLng(latitude, longitude),
                name = name,
                addressComponents = addressComponents,
                openingHours = openingHours,
                bitmap = bitmap,
            )

            is PlaceAutocompleteResult.StreetAddress -> GetPlaceDataStatus.StreetAddressData(
                latLng = LatLng(latitude, longitude),
                addressComponents = addressComponents,
            )

            is PlaceAutocompleteResult.Location -> GetPlaceDataStatus.PlaceTypeException
            PlaceAutocompleteResult.Error -> GetPlaceDataStatus.UnknownException
        }
    }
}