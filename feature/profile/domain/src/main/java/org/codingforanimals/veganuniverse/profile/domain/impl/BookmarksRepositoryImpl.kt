package org.codingforanimals.veganuniverse.profile.domain.impl

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.codingforanimals.veganuniverse.profile.domain.BookmarksRepository
import org.codingforanimals.veganuniverse.profile.domain.model.Bookmarks
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableType
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.services.FetchRecipeService

class BookmarksRepositoryImpl(
    private val recipesService: FetchRecipeService,
    private val profileLookupsService: ProfileLookupsService,
) : BookmarksRepository {

    override suspend fun getBookmarks(userId: String): Bookmarks = coroutineScope {
        val recipesIds = async {
            profileLookupsService.getContentSavedByUser(
                saveableType = SaveableType.BOOKMARK,
                contentType = SaveableContentType.RECIPE,
                userId = userId
            )
        }
        return@coroutineScope Bookmarks(
            recipesIds = recipesIds.await(),
        )
    }

    override suspend fun getBookmarkedRecipes(ids: List<String>): List<Recipe> {
        return if (ids.isNotEmpty()) recipesService.byIds(ids) else emptyList()
    }

}