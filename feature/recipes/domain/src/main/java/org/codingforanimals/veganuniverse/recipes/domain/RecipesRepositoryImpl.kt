package org.codingforanimals.veganuniverse.recipes.domain

import org.codingforanimals.veganuniverse.recipes.api.RecipesApi
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams

internal const val LRU_DEFAULT_SIZE = 4 * 1024 * 1024

internal class RecipesRepositoryImpl(
    private val recipesApi: RecipesApi,
    private val recipeListCache: RecipeListCache,
    private val recipeCache: RecipeCache,
) : RecipesRepository {
    override suspend fun fetchRecipes(
        params: RecipeQueryParams,
        cacheKey: String?,
    ): List<Recipe> {
        val recipes = recipesApi.fetchRecipes(params)
        if (cacheKey != null) {
            recipeListCache.appendRecipes(cacheKey, recipes)
        }
        return recipes
    }

    override suspend fun getCachedRecipes(key: String): List<Recipe>? {
        return recipeListCache.getRecipes(key)
    }

    override suspend fun fetchRecipe(id: String): Recipe? {
        val recipe = recipesApi.fetchRecipe(id)
        if (recipe != null) {
            recipeCache.putRecipe(recipe)
        }
        return recipe
    }

    override suspend fun getCachedRecipe(id: String): Recipe? {
        return recipeCache.getRecipe(id)
    }
}