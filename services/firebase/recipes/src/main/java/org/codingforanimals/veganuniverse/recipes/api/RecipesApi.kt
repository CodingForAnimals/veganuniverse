package org.codingforanimals.veganuniverse.recipes.api

import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

interface RecipesApi {
    suspend fun submitRecipe(recipeForm: RecipeForm)
}