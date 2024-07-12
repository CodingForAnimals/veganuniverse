package org.codingforanimals.veganuniverse.place.presentation.listing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.codingforanimals.veganuniverse.commons.place.presentation.model.administrativeArea
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceType
import org.codingforanimals.veganuniverse.commons.profile.domain.model.ListingType
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.GetProfile
import org.codingforanimals.veganuniverse.place.listing.usecase.QueryPlacesByIds
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.home.model.PlaceCardUI
import org.codingforanimals.veganuniverse.place.presentation.navigation.LISTING_TYPE

internal class PlaceListingViewModel(
    savedStateHandle: SavedStateHandle,
    queryPlacesByIds: QueryPlacesByIds,
    getProfile: GetProfile,
) : ViewModel() {
    private val listingType = savedStateHandle.get<String>(LISTING_TYPE)

    val title: Int? = when (ListingType.fromString(listingType)) {
        ListingType.CONTRIBUTIONS -> R.string.contributed_places
        ListingType.BOOKMARKS -> R.string.bookmarked_places
        null -> null
    }

    val emptyResultsTextRes: Int? = when (ListingType.fromString(listingType)) {
        ListingType.CONTRIBUTIONS -> R.string.empty_contributed_places_message
        ListingType.BOOKMARKS -> R.string.empty_bookmarked_places_message
        null -> null
    }

    val places = flow {
        val ids = with(getProfile()) {
            when (ListingType.fromString(listingType)) {
                ListingType.CONTRIBUTIONS -> contributions.places
                ListingType.BOOKMARKS -> bookmarks.places
                null -> return@flow
            }
        }
        emitAll(
            queryPlacesByIds(ids).cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { it.toCard() }
            }
        )
    }

    private fun Place.toCard(): PlaceCardUI {
        return PlaceCardUI(
            geoHash = geoHash ?: "",
            name = name ?: "",
            rating = rating,
            streetAddress = addressComponents?.streetAddress ?: "",
            administrativeArea = addressComponents?.administrativeArea ?: "",
            type = type ?: PlaceType.STORE,
            tags = tags.orEmpty(),
            imageUrl = imageUrl,
            markerState = MarkerState(LatLng(latitude, longitude))
        )
    }
}
