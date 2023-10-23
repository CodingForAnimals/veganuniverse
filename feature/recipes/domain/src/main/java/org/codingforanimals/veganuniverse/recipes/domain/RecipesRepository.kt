package org.codingforanimals.veganuniverse.recipes.domain

import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams

interface RecipesRepository {
    suspend fun fetchRecipes(params: RecipeQueryParams, cacheKey: String? = null): List<Recipe>
    suspend fun getCachedRecipes(key: String): List<Recipe>?
    suspend fun fetchRecipe(id: String): Recipe?
    suspend fun getCachedRecipe(id: String): Recipe?
    suspend fun isRecipeLikedByUser(recipeId: String, userId: String): Boolean
    suspend fun isRecipeBookmarkedByUser(recipeId: String, userId: String): Boolean
    suspend fun updateLikeReturningCurrent(like: Boolean, recipeId: String, userId: String): Boolean
    suspend fun updateBookmarkReturningCurrent(
        bookmark: Boolean,
        recipeId: String,
        userId: String,
    ): Boolean
}

