package org.codingforanimals.veganuniverse.recipes.domain

import org.codingforanimals.veganuniverse.recipes.api.RecipesApi
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

internal class RecipesRepositoryImpl(
    private val recipesApi: RecipesApi,
) : RecipesRepository {
    override suspend fun submitRecipe(recipeForm: RecipeForm) {
        recipesApi.submitRecipe(recipeForm)
    }
}