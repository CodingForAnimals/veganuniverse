package org.codingforanimals.veganuniverse.recipes.domain.usecase

import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class ReportRecipe(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(recipeId: String): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to report a recipe"
        }
        recipeRepository.reportRecipe(recipeId, user.id)
    }
}
