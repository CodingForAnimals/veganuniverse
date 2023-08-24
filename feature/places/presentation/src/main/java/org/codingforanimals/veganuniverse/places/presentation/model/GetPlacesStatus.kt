package org.codingforanimals.veganuniverse.places.presentation.model

internal sealed class GetPlacesStatus {
    object Loading : GetPlacesStatus()
    object Error : GetPlacesStatus()
    data class Success(val places: List<org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard>) :
        GetPlacesStatus()
}