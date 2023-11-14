package org.codingforanimals.veganuniverse.recipes.domain.impl

import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.services.firebase.FetchRecipeService
import org.codingforanimals.veganuniverse.recipes.services.firebase.IncrementRecipeLikesService
import org.codingforanimals.veganuniverse.recipes.services.firebase.RecipesQueryService
import org.codingforanimals.veganuniverse.recipes.storage.RecipeListCache

internal class RecipesRepositoryImpl(
    private val recipesQueryService: RecipesQueryService,
    private val fetchRecipeService: FetchRecipeService,
    private val recipeListCache: RecipeListCache,
    private val profileLookupsService: ProfileLookupsService,
    private val recipeLikesService: IncrementRecipeLikesService,
) : RecipesRepository {

    override suspend fun fetchRecipes(
        params: RecipeQueryParams,
        cacheKey: String?,
    ): List<Recipe> {
        val recipes = recipesQueryService(params)
        if (cacheKey != null) {
            recipeListCache.appendRecipes(cacheKey, recipes.map { it.id })
        }
        return recipes
    }

    override suspend fun getRecipeList(key: String): List<Recipe>? {
        return recipeListCache.getRecipesIds(key)?.let { fetchRecipeService.byIds(it) }
    }

    override suspend fun fetchRecipe(id: String): Recipe? {
        return fetchRecipeService.byId(id)
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
            recipeLikesService.increase(recipeId)
            true
        } else {
            profileLookupsService.removeContent(
                contentId = recipeId,
                saveableType = SaveableType.LIKE,
                contentType = SaveableContentType.RECIPE,
                userId = userId
            )
            recipeLikesService.decrease(recipeId)
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