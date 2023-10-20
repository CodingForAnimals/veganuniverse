package org.codingforanimals.veganuniverse.create.domain.recipes

import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm
import org.codingforanimals.veganuniverse.recipes.services.RecipesService

internal class RecipeCreatorImpl(
    private val recipesService: RecipesService,
) : RecipeCreator {
    override suspend fun createRecipe(recipeForm: RecipeForm) {
        recipesService.submitRecipe(recipeForm)
    }
}