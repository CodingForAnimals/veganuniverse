package org.codingforanimals.veganuniverse.create.domain.recipes

import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm
import org.codingforanimals.veganuniverse.recipes.services.SubmitRecipeService

internal class RecipeCreatorImpl(
    private val submitRecipeService: SubmitRecipeService,
) : RecipeCreator {
    override suspend fun createRecipe(recipeForm: RecipeForm) {
        submitRecipeService(recipeForm)
    }
}