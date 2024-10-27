package org.codingforanimals.veganuniverse.place.presentation.listing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.GetProfile
import org.codingforanimals.veganuniverse.commons.ui.listings.ListingType
import org.codingforanimals.veganuniverse.place.listing.usecase.QueryPlacesByIds
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.model.toCard
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
                ListingType.CONTRIBUTIONS -> this?.contributions?.places.orEmpty()
                ListingType.BOOKMARKS -> this?.bookmarks?.places.orEmpty()
                null -> return@flow
            }
        }
        emitAll(
            queryPlacesByIds(ids).cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { it.toCard() }
            }
        )
    }
}
