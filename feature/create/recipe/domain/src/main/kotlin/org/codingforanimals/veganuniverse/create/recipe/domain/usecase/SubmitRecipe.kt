package org.codingforanimals.veganuniverse.create.recipe.domain.usecase

import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.create.recipe.domain.model.RecipeForm

class SubmitRecipe(
    private val recipeRepository: RecipeRepository,
    private val profileRecipeUseCases: ProfileContentUseCases,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(recipeForm: RecipeForm): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to submit a recipe"
        }

        val recipeFormAsModel = recipeForm.toModel(user.id, user.name)
        recipeRepository.insertRecipe(recipeFormAsModel, recipeForm.imageModel).also {
            profileRecipeUseCases.addContribution(it.id!!)
        }
    }

    private fun RecipeForm.toModel(userId: String, username: String): Recipe {
        return Recipe(
            id = null,
            userId = userId,
            username = username,
            name = name,
            description = description,
            tags = tags,
            ingredients = ingredients,
            steps = steps,
            prepTime = prepTime,
            servings = servings,
            likes = 0,
            createdAt = null,
            imageUrl = null,
            validated = false,
        )
    }
}
