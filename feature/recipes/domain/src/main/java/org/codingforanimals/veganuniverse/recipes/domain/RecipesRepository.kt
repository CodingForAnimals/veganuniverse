package org.codingforanimals.veganuniverse.recipes.domain

import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

interface RecipesRepository {
    suspend fun submitRecipe(recipeForm: RecipeForm)
}

