package org.codingforanimals.veganuniverse.profile.home.presentation.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.profile.home.domain.BookmarksRepository
import org.codingforanimals.veganuniverse.profile.home.presentation.entity.toCard
import org.codingforanimals.veganuniverse.profile.home.presentation.model.ProfileFeatureContentState
import org.codingforanimals.veganuniverse.profile.home.presentation.model.ProfileFeatureItemsState
import org.codingforanimals.veganuniverse.ui.cards.SimpleCardItem

private const val TAG = "GetRecipeBookmarksUseCa"

class GetBookmarksUseCase(
    private val bookmarksRepository: BookmarksRepository,
) {
    suspend operator fun invoke(userId: String): ProfileFeatureItemsState {
        val allBookmarks = bookmarksRepository.getBookmarks(userId)
        return ProfileFeatureItemsState(
            recipes = getBookmarkedRecipes(allBookmarks.recipesIds),
            places = getBookmarkedPlaces(allBookmarks.placesIds)
        )
    }

    private suspend fun getBookmarkedRecipes(recipesIds: List<String>): ProfileFeatureContentState<SimpleCardItem> {
        return try {
            val lastTwoRecipesIds = recipesIds.getLastTwo()
            val recipes = bookmarksRepository.getBookmarkedRecipes(lastTwoRecipesIds)
            ProfileFeatureContentState.Success(
                recipes.map {
                    SimpleCardItem(
                        id = it.id,
                        title = it.title,
                        imageRef = it.imageRef
                    )
                }
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            ProfileFeatureContentState.Error
        }
    }

    private suspend fun getBookmarkedPlaces(placesIds: List<String>): ProfileFeatureContentState<PlaceCardItem> {
        return try {
            val lastTwoPlacesIds = placesIds.getLastTwo()
            val cards = bookmarksRepository
                .getBookmarkedPlaces(lastTwoPlacesIds)
                .mapNotNull { it.toCard() }
            ProfileFeatureContentState.Success(cards)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            ProfileFeatureContentState.Error
        }
    }

    private fun <T> List<T>.getLastTwo(): List<T> {
        val last = getOrNull(size - 1)
        val previous = getOrNull(size - 2)
        return listOfNotNull(last, previous)
    }
}