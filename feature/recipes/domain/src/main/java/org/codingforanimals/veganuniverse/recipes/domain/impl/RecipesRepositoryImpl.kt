package org.codingforanimals.veganuniverse.recipes.domain.impl

import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.services.firebase.model.SaveableType
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.services.FetchRecipeService
import org.codingforanimals.veganuniverse.recipes.services.RecipesQueryService
import org.codingforanimals.veganuniverse.recipes.storage.RecipeCache
import org.codingforanimals.veganuniverse.recipes.storage.RecipeListCache

internal class RecipesRepositoryImpl(
    private val recipesQueryService: RecipesQueryService,
    private val fetchRecipeService: FetchRecipeService,
    private val recipeListCache: RecipeListCache,
    private val recipeCache: RecipeCache,
    private val profileLookupsService: ProfileLookupsService,
) : RecipesRepository {

    override suspend fun fetchRecipes(
        params: RecipeQueryParams,
        cacheKey: String?,
    ): List<Recipe> {
        val recipes = recipesQueryService(params)
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
        return profileLookupsService.isContentSavedByUser(
            contentId = recipeId,
            contentType = SaveableContentType.RECIPE,
            saveableType = SaveableType.LIKE,
            userId = userId,
        )
    }

    override suspend fun isRecipeBookmarkedByUser(recipeId: String, userId: String): Boolean {
        return profileLookupsService.isContentSavedByUser(
            contentId = recipeId,
            saveableType = SaveableType.BOOKMARK,
            contentType = SaveableContentType.RECIPE,
            userId = userId
        )
    }

    override suspend fun updateLikeReturningCurrent(
        like: Boolean,
        recipeId: String,
        userId: String,
    ): Boolean {
        return if (like) {
            profileLookupsService.saveContent(
                contentId = recipeId,
                saveableType = SaveableType.LIKE,
                contentType = SaveableContentType.RECIPE,
                userId = userId
            )
            true
        } else {
            profileLookupsService.removeContent(
                contentId = recipeId,
                saveableType = SaveableType.LIKE,
                contentType = SaveableContentType.RECIPE,
                userId = userId
            )
            false
        }
    }

    override suspend fun updateBookmarkReturningCurrent(
        bookmark: Boolean,
        recipeId: String,
        userId: String,
    ): Boolean {
        return if (bookmark) {
            profileLookupsService.saveContent(
                contentId = recipeId,
                saveableType = SaveableType.BOOKMARK,
                contentType = SaveableContentType.RECIPE,
                userId = userId
            )
            true
        } else {
            profileLookupsService.removeContent(
                contentId = recipeId,
                saveableType = SaveableType.BOOKMARK,
                contentType = SaveableContentType.RECIPE,
                userId = userId
            )
            false
        }
    }
}