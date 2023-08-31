package org.codingforanimals.veganuniverse.places.presentation.model

internal sealed class GetPlacesStatus {
    data object Loading : GetPlacesStatus()
    data object Error : GetPlacesStatus()
    data class Success(val places: List<org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard>) :
        GetPlacesStatus()
}