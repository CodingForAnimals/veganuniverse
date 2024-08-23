package org.codingforanimals.veganuniverse.recipes.domain.usecase

import android.util.Log
import kotlinx.coroutines.delay
import org.codingforanimals.veganuniverse.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.profile.model.ToggleResult
import org.codingforanimals.veganuniverse.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.recipe.model.Recipe
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser

private const val TAG = "RecipeDetailsUseCases"

class RecipeDetailsUseCases(
    private val getCurrentUser: GetCurrentUser,
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
        getCurrentUser() ?: return ToggleResult.GuestUser(currentValue)
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
}
