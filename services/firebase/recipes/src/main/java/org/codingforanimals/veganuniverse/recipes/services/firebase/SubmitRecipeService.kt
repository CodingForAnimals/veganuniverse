package org.codingforanimals.veganuniverse.recipes.services.firebase

import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

interface SubmitRecipeService {
    suspend operator fun invoke(recipeForm: RecipeForm): String
}

