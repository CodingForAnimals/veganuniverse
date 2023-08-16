package org.codingforanimals.places.presentation.model

import org.codingforanimals.places.presentation.entity.PlaceViewEntity

internal sealed class GetPlacesStatus {
    object Loading : GetPlacesStatus()
    object Error : GetPlacesStatus()
    data class Success(val places: List<PlaceViewEntity>) : GetPlacesStatus()
}