package org.codingforanimals.veganuniverse.recipes.services

import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams

interface RecipesService {
    suspend fun submitRecipe(recipeForm: RecipeForm)
    suspend fun fetchRecipes(params: RecipeQueryParams): List<Recipe>
    suspend fun fetchRecipe(id: String): Recipe?
}