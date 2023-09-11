package org.codingforanimals.veganuniverse.places.presentation.home.state

import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.veganuniverse.places.presentation.home.entity.PlaceCardViewEntity
import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard

internal sealed class PlacesState {
    data object Error : PlacesState()
    data object Loading : PlacesState()
    data class Success(
        val searchCenter: LatLng = LatLng(0.0, 0.0),
        val searchRadiusInKm: Double = 0.0,
        val zoom: Float = 0f,
        private val rawContent: List<PlaceCardViewEntity> = emptyList(),
        private val filterState: FilterState = FilterState(),
    ) : PlacesState() {
        val content: List<PlaceCardViewEntity> =
            rawContent
                .filter { it.card.isMatchingType && it.card.containsAllTags }

        private val PlaceCard.isMatchingType: Boolean
            get() = if (filterState.activePlaceType == null) true else {
                type == filterState.activePlaceType
            }

        private val PlaceCard.containsAllTags: Boolean
            get() = tags.containsAll(filterState.activePlaceTags)
    }
}