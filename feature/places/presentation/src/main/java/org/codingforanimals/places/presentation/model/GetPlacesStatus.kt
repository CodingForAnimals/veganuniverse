package org.codingforanimals.places.presentation.model

internal sealed class GetPlacesStatus {
    object Loading : GetPlacesStatus()
    object Error : GetPlacesStatus()
    data class Success(val places: List<PlaceViewEntity>) : GetPlacesStatus()
}