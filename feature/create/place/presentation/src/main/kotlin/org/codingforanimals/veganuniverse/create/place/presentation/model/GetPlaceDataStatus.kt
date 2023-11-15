package org.codingforanimals.veganuniverse.create.place.presentation.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.veganuniverse.places.entity.AddressComponents

sealed class GetPlaceDataStatus {
    data object Loading : GetPlaceDataStatus()
    data class EstablishmentData(
        val latLng: LatLng,
        val name: String,
        val addressComponents: AddressComponents,
        val openingHours: List<OpeningHours>,
        val bitmap: Bitmap?,
    ) : GetPlaceDataStatus()

    data class StreetAddressData(
        val latLng: LatLng,
        val addressComponents: AddressComponents,
    ) : GetPlaceDataStatus()

    data object PlaceTypeException : GetPlaceDataStatus()
    data object UnknownException : GetPlaceDataStatus()
}