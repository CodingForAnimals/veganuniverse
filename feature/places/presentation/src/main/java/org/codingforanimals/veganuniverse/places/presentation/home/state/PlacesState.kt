package org.codingforanimals.veganuniverse.places.presentation.home.state

import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.veganuniverse.core.ui.place.PlaceSorter
import org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard

internal sealed class PlacesState {
    data object Error : PlacesState()
    data object Loading : PlacesState()
    data class Success(
        val searchCenter: LatLng = LatLng(0.0, 0.0),
        val searchRadiusInKm: Double = 0.0,
        val zoom: Float = 0f,
        private val rawContent: List<PlaceCard> = emptyList(),
        private val filterState: FilterState = FilterState(),
    ) : PlacesState() {
        val content: List<PlaceCard> =
            rawContent
                .filter { it.isMatchingType && it.containsAllTags }
                .sortedWith(getSorter)

        private val getSorter
            get() =
                when (filterState.sorter) {
                    PlaceSorter.NAME -> Comparator<PlaceCard> { t, t2 ->
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

        private val PlaceCard.isMatchingType: Boolean
            get() = if (filterState.activePlaceType == null) true else {
                type == filterState.activePlaceType
            }

        private val PlaceCard.containsAllTags: Boolean
            get() = tags.containsAll(filterState.activePlaceTags)
    }
}