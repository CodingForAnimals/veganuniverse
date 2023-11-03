package org.codingforanimals.veganuniverse.profile.domain

import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.profile.domain.model.Bookmarks
import org.codingforanimals.veganuniverse.recipes.entity.Recipe

interface BookmarksRepository {
    suspend fun getBookmarks(userId: String): Bookmarks
    suspend fun getBookmarkedRecipes(ids: List<String>): List<Recipe>
    suspend fun getBookmarkedPlaces(ids: List<String>): List<Place>
}

