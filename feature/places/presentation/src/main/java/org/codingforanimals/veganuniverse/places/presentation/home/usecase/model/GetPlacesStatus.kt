package org.codingforanimals.veganuniverse.places.presentation.home.usecase.model

import org.codingforanimals.veganuniverse.places.presentation.home.entity.PlaceCardViewEntity

internal sealed class GetPlacesStatus {
    data object Loading : GetPlacesStatus()
    data object Error : GetPlacesStatus()
    data class Success(val places: List<PlaceCardViewEntity>) :
        GetPlacesStatus()
}