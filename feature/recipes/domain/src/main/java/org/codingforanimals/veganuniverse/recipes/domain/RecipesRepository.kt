package org.codingforanimals.veganuniverse.recipes.domain

import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams

interface RecipesRepository {
    suspend fun fetchRecipes(params: RecipeQueryParams, cacheKey: String? = null): List<Recipe>
    suspend fun getCachedRecipes(key: String): List<Recipe>?
    suspend fun fetchRecipe(id: String): Recipe?
    suspend fun getCachedRecipe(id: String): Recipe?
}

