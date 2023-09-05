package org.codingforanimals.veganuniverse.places.presentation.home.usecase.model

import com.google.android.gms.maps.model.LatLng

sealed class GetLocationDataStatus {
    data object Error : GetLocationDataStatus()
    data class Location(val latLng: LatLng) : GetLocationDataStatus()
}