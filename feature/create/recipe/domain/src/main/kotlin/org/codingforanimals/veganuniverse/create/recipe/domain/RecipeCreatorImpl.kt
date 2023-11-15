package org.codingforanimals.veganuniverse.create.recipe.domain

import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm
import org.codingforanimals.veganuniverse.recipes.services.firebase.SubmitRecipeService

internal class RecipeCreatorImpl(
    private val submitRecipeService: SubmitRecipeService,
    private val profileLookupsService: ProfileLookupsService,
) : RecipeCreator {
    override suspend fun createRecipe(recipeForm: RecipeForm) {
        val recipeId = submitRecipeService(recipeForm)
        profileLookupsService.saveContent(
            contentId = recipeId,
            saveableType = SaveableType.CONTRIBUTION,
            contentType = SaveableContentType.RECIPE,
            userId = recipeForm.userId,
        )
    }
}