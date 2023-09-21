package org.codingforanimals.veganuniverse.create.domain.recipes

import org.codingforanimals.veganuniverse.recipes.api.RecipesApi
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

internal class RecipeCreatorImpl(
    private val recipesApi: RecipesApi,
) : RecipeCreator {
    override suspend fun createRecipe(recipeForm: RecipeForm) {
        recipesApi.submitRecipe(recipeForm)
    }
}