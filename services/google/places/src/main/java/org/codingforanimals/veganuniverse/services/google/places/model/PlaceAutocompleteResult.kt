package org.codingforanimals.veganuniverse.services.google.places.model

import android.graphics.Bitmap
import org.codingforanimals.veganuniverse.places.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.entity.OpeningHours

sealed class PlaceAutocompleteResult {
    data class Establishment(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val openingHours: List<OpeningHours>,
        val addressComponents: AddressComponents,
        val bitmap: Bitmap?,
    ) : PlaceAutocompleteResult()

    data class StreetAddress(
        val latitude: Double,
        val longitude: Double,
        val addressComponents: AddressComponents,
    ) : PlaceAutocompleteResult()

    data class Location(
        val latitude: Double,
        val longitude: Double,
    ) : PlaceAutocompleteResult()

    data object Error : PlaceAutocompleteResult()
}
