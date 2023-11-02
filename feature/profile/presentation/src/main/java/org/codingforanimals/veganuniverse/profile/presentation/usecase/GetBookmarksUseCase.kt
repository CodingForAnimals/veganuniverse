package org.codingforanimals.veganuniverse.profile.presentation.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.profile.domain.BookmarksRepository
import org.codingforanimals.veganuniverse.profile.presentation.model.Bookmarks
import org.codingforanimals.veganuniverse.profile.presentation.model.ProfileFeatureContentState
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

private const val TAG = "GetRecipeBookmarksUseCa"

internal class GetBookmarksUseCase(
    private val bookmarksRepository: BookmarksRepository,
) {
    suspend operator fun invoke(userId: String): Bookmarks {
        val allBookmarks = bookmarksRepository.getBookmarks(userId)
        return Bookmarks(
            recipes = getBookmarkedRecipes(allBookmarks.recipesIds),
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

    private fun <T> List<T>.getLastTwo(): List<T> {
        val last = getOrNull(size - 1)
        val previous = getOrNull(size - 2)
        return listOfNotNull(last, previous)
    }
}