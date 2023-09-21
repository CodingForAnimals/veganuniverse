package org.codingforanimals.veganuniverse.create.domain.recipes

import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

interface RecipeCreator {
    suspend fun createRecipe(recipeForm: RecipeForm)
}