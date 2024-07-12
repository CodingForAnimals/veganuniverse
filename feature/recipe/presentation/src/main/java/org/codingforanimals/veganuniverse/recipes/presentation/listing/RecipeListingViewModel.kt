package org.codingforanimals.veganuniverse.recipes.presentation.listing

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.commons.profile.domain.model.ListingType
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.GetProfile
import org.codingforanimals.veganuniverse.recipes.domain.usecase.QueryRecipesById
import org.codingforanimals.veganuniverse.recipes.presentation.R

internal class RecipeListingViewModel(
    listingType: String?,
    queryRecipesById: QueryRecipesById,
    getProfile: GetProfile,
) : ViewModel() {

    val title: Int? = when (ListingType.fromString(listingType)) {
        ListingType.CONTRIBUTIONS -> R.string.your_recipes
        ListingType.BOOKMARKS -> R.string.bookmarked_recipes
        null -> null
    }

    val emptyResultsTextRes: Int? = when (ListingType.fromString(listingType)) {
        ListingType.CONTRIBUTIONS -> R.string.empty_contributed_recipes_message
        ListingType.BOOKMARKS -> R.string.empty_bookmarked_recipes_message
        null -> null
    }

    val recipes = flow {
        val ids = with(getProfile()) {
            when (ListingType.fromString(listingType)) {
                ListingType.CONTRIBUTIONS -> contributions.recipes
                ListingType.BOOKMARKS -> bookmarks.recipes
                null -> return@flow
            }
        }
        emitAll(queryRecipesById(ids))
    }
}