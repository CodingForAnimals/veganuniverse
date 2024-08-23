package org.codingforanimals.veganuniverse.recipes.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.profile.shared.model.ToggleResult
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private const val TAG = "RecipeDetailsUseCases"

class RecipeDetailsUseCases(
    val reportRecipe: ReportRecipe,
    val editRecipe: EditRecipe,
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val recipeRepository: RecipeRepository,
    private val profileRecipeUseCases: ProfileContentUseCases,
) {
    suspend fun getRecipe(id: String): Recipe? {
        return recipeRepository.getRecipeById(id)
    }

    suspend fun isLiked(id: String): Boolean {
        return profileRecipeUseCases.isLiked(id)
    }

    suspend fun toggleLike(recipeId: String, currentValue: Boolean): ToggleResult {
        flowOnCurrentUser().firstOrNull() ?: return ToggleResult.UnexpectedError(currentValue)
        runCatching {
            recipeRepository.increaseOrDecreaseLike(recipeId, !currentValue)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            return ToggleResult.UnexpectedError(currentValue)
        }
        return profileRecipeUseCases.toggleLike(recipeId, currentValue)
    }

    suspend fun isBookmarked(id: String): Boolean {
        return profileRecipeUseCases.isBookmarked(id)
    }

    suspend fun toggleBookmark(recipeId: String, currentValue: Boolean): ToggleResult {
        return profileRecipeUseCases.toggleBookmark(recipeId, currentValue)
    }

    suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return runCatching {
            recipeRepository.deleteRecipeById(recipeId)
            profileRecipeUseCases.removeContribution(recipeId)
        }
    }
}

