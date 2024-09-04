package org.codingforanimals.veganuniverse.product.presentation.listing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.commons.ui.listings.ListingType
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductListingUseCases
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination

internal class ProductListingViewModel(
    savedStateHandle: SavedStateHandle,
    useCases: ProductListingUseCases,
) : ViewModel() {

    private val listingType = savedStateHandle.listingType

    val title = when (listingType) {
        ListingType.CONTRIBUTIONS -> R.string.contributed_products
        ListingType.BOOKMARKS -> R.string.bookmarked_products
    }

    val emptyResultsTextRes = when (listingType) {
        ListingType.CONTRIBUTIONS -> R.string.empty_contributed_products_message
        ListingType.BOOKMARKS -> R.string.empty_bookmarked_products_message
    }

    val products = flow {
        val ids = when (listingType) {
            ListingType.CONTRIBUTIONS -> useCases.getContributionsIds()
            ListingType.BOOKMARKS -> useCases.getBookmarksIds()
        }
        emitAll(
            useCases.queryProductsByIds(ids)
        )
    }

    private val SavedStateHandle.listingType: ListingType
        get() = ListingType.fromString(get(ProductDestination.Listing.TYPE))
            ?: ListingType.CONTRIBUTIONS
}