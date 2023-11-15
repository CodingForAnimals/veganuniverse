package org.codingforanimals.veganuniverse.create.recipe.domain

import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

interface RecipeCreator {
    suspend fun createRecipe(recipeForm: RecipeForm)
}