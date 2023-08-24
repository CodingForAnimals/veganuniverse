package org.codingforanimals.veganuniverse.places.presentation.home.state

import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.veganuniverse.core.ui.place.PlaceSorter

internal sealed class PlacesState {
    object Error : PlacesState()
    object Loading : PlacesState()
    data class Success(
        val searchCenter: LatLng = LatLng(0.0, 0.0),
        val searchRadiusInKm: Double = 0.0,
        val zoom: Float = 0f,
        private val rawContent: List<org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard> = emptyList(),
        private val filterState: FilterState = FilterState(),
    ) : PlacesState() {
        val content: List<org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard> =
            rawContent
                .filter { it.isMatchingType && it.containsAllTags }
                .sortedWith(getSorter)

        private val getSorter
            get() =
                when (filterState.sorter) {
                    PlaceSorter.NAME -> Comparator<org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard> { t, t2 ->
                        compareValues(
                            t.name,
                            t2.name
                        )
                    }

                    PlaceSorter.RATING -> Comparator { t, t2 ->
                        compareValues(
                            t2.rating,
                            t.rating,
                        )
                    }
                    PlaceSorter.REVIEWS -> Comparator { t, t2 ->
                        compareValues(
                            t.name,
                            t2.name
                        )
                    }
                    PlaceSorter.DATE -> Comparator { t, t2 ->
                        compareValues(
                            t2.timestamp,
                            t.timestamp,
                        )
                    }
                }

        private val org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard.isMatchingType: Boolean
            get() = if (filterState.activePlaceType == null) true else {
                type == filterState.activePlaceType
            }

        private val org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard.containsAllTags: Boolean
            get() = tags.containsAll(filterState.activePlaceTags)
    }
}