package org.codingforanimals.veganuniverse.create.presentation.place.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

sealed class GetPlaceDataStatus {
    data object Loading : GetPlaceDataStatus()
    data class EstablishmentData(
        val latLng: LatLng,
        val name: String,
        val streetAddress: String,
        val locality: String,
        val primaryAdminArea: String,
        val secondaryAdminArea: String,
        val country: String,
        val openingHours: List<OpeningHours>,
    ) : GetPlaceDataStatus()

    data class EstablishmentPicture(
        val bitmap: Bitmap,
    ) : GetPlaceDataStatus()

    data class StreetAddressData(
        val latLng: LatLng,
        val address: String,
    ) : GetPlaceDataStatus()

    data object MissingCriticalFieldException : GetPlaceDataStatus()
    data object EstablishmentPictureException : GetPlaceDataStatus()
    data object PlaceTypeException : GetPlaceDataStatus()
    data object UnknownException : GetPlaceDataStatus()
}