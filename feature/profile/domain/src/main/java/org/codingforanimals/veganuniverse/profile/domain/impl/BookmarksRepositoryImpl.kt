package org.codingforanimals.veganuniverse.profile.domain.impl

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.services.firebase.FetchPlaceService
import org.codingforanimals.veganuniverse.profile.domain.BookmarksRepository
import org.codingforanimals.veganuniverse.profile.domain.model.Bookmarks
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableType
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.services.firebase.FetchRecipeService

class BookmarksRepositoryImpl(
    private val recipesService: FetchRecipeService,
    private val placeService: FetchPlaceService,
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

        val placesIds = async {
            profileLookupsService.getContentSavedByUser(
                saveableType = SaveableType.BOOKMARK,
                contentType = SaveableContentType.PLACE,
                userId = userId,
            )
        }
        return@coroutineScope Bookmarks(
            recipesIds = recipesIds.await(),
            placesIds = placesIds.await(),
        )
    }

    override suspend fun getBookmarkedRecipes(ids: List<String>): List<Recipe> {
        return if (ids.isNotEmpty()) recipesService.byIds(ids) else emptyList()
    }

    override suspend fun getBookmarkedPlaces(ids: List<String>): List<Place> {
        return if (ids.isNotEmpty()) placeService.byIds(ids) else emptyList()
    }

}