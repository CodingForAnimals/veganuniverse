package org.codingforanimals.veganuniverse.recipes.domain.impl

import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.services.FetchRecipeService
import org.codingforanimals.veganuniverse.recipes.services.FetchRecipesService
import org.codingforanimals.veganuniverse.recipes.services.RecipeLikesService
import org.codingforanimals.veganuniverse.recipes.storage.RecipeCache
import org.codingforanimals.veganuniverse.recipes.storage.RecipeListCache

internal class RecipesRepositoryImpl(
    private val fetchRecipesService: FetchRecipesService,
    private val fetchRecipeService: FetchRecipeService,
    private val recipeListCache: RecipeListCache,
    private val recipeCache: RecipeCache,
    private val recipeLikesService: RecipeLikesService,
) : RecipesRepository {
    override suspend fun fetchRecipes(
        params: RecipeQueryParams,
        cacheKey: String?,
    ): List<Recipe> {
        val recipes = fetchRecipesService(params)
        if (cacheKey != null) {
            recipeListCache.appendRecipes(cacheKey, recipes)
        }
        return recipes
    }

    override suspend fun getCachedRecipes(key: String): List<Recipe>? {
        return recipeListCache.getRecipes(key)
    }

    override suspend fun fetchRecipe(id: String): Recipe? {
        val recipe = fetchRecipeService(id)
        if (recipe != null) {
            recipeCache.putRecipe(recipe)
        }
        return recipe
    }

    override suspend fun getCachedRecipe(id: String): Recipe? {
        return recipeCache.getRecipe(id)
    }

    override suspend fun isRecipeLikedByUser(recipeId: String, userId: String): Boolean {
        return recipeLikesService.isRecipeLikedByUser(recipeId, userId)
    }

    override suspend fun isRecipeBookmarkedByUser(recipeId: String, userId: String): Boolean {
        return recipeLikesService.isRecipeBookmarkedByUser(recipeId, userId)
    }

    override suspend fun updateLikeReturningCurrent(
        like: Boolean,
        recipeId: String,
        userId: String,
    ): Boolean {
        return if (like) {
            recipeLikesService.likeReturningCurrent(recipeId, userId)
        } else {
            recipeLikesService.dislikeReturningCurrent(recipeId, userId)
        }
    }

    override suspend fun updateBookmarkReturningCurrent(
        bookmark: Boolean,
        recipeId: String,
        userId: String,
    ): Boolean {
        return if (bookmark) {
            recipeLikesService.bookmarkReturningCurrent(recipeId, userId)
        } else {
            recipeLikesService.unbookmarkReturningCurrent(recipeId, userId)
        }
    }
}