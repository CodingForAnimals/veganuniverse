package org.codingforanimals.places.presentation.home.state

import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.places.presentation.model.PlaceViewEntity
import org.codingforanimals.veganuniverse.core.ui.place.PlaceSorter

sealed class PlacesState {
    object Error : PlacesState()
    object Loading : PlacesState()
    data class Success(
        val searchCenter: LatLng = LatLng(0.0, 0.0),
        val searchRadiusInMeters: Double = 0.0,
        val zoom: Float = 0f,
        private val rawContent: List<PlaceViewEntity> = emptyList(),
        private val filterState: FilterState = FilterState(),
    ) : PlacesState() {
        val content: List<PlaceViewEntity> = rawContent
            .filter { it.isMatchingType && it.containsAllTags }
            .sortedWith(getSorter)

        private val getSorter
            get() =
                when (filterState.sorter) {
                    PlaceSorter.NAME -> Comparator<PlaceViewEntity> { t, t2 ->
                        compareValues(
                            t.name,
                            t2.name
                        )
                    }
                    PlaceSorter.RATING -> Comparator { t, t2 ->
                        compareValues(
                            t.rating,
                            t2.rating
                        )
                    }
                    PlaceSorter.REVIEWS -> Comparator { t, t2 ->
                        compareValues(
                            t.reviewCount,
                            t2.reviewCount
                        )
                    }
                    PlaceSorter.DATE -> Comparator { t, t2 ->
                        compareValues(
                            t2.timestamp,
                            t.timestamp,
                        )
                    }
                }

        private val PlaceViewEntity.isMatchingType: Boolean
            get() = if (filterState.activePlaceType == null) true else {
                type == filterState.activePlaceType
            }

        private val PlaceViewEntity.containsAllTags: Boolean
            get() = tags.containsAll(filterState.activePlaceTags)
    }
}