package org.codingforanimals.veganuniverse.recipes.domain.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe

private const val TAG = "RecipeDetailsUseCases"

class RecipeDetailsUseCases(
    val reportRecipe: ReportRecipe,
    private val recipeRepository: RecipeRepository,
    private val profileRecipeUseCases: ProfileContentUseCases,
) {
    suspend fun getRecipe(id: String): Result<Recipe?> = runCatching {
        recipeRepository.getRecipeById(id)
    }.onFailure {
        Log.e(TAG, "Error getting recipe", it)
        Analytics.logNonFatalException(it)
    }

    suspend fun isLiked(id: String): Boolean {
        return profileRecipeUseCases.isLiked(id)
    }

    suspend fun toggleLike(recipeId: String, currentValue: Boolean): Result<Boolean> {
        return runCatching {
            recipeRepository.increaseOrDecreaseLike(recipeId, !currentValue)
            profileRecipeUseCases.toggleLike(recipeId, currentValue)
        }.getOrElse {
            Log.e(TAG, "Error toggling recipe like", it)
            Analytics.logNonFatalException(it)
            Result.failure(it)
        }
    }

    suspend fun isBookmarked(id: String): Boolean {
        return profileRecipeUseCases.isBookmarked(id)
    }

    suspend fun toggleBookmark(recipeId: String, currentValue: Boolean): Result<Boolean> {
        return profileRecipeUseCases.toggleBookmark(recipeId, currentValue)
    }

    suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return runCatching {
            recipeRepository.deleteRecipeById(recipeId).also {
                profileRecipeUseCases.removeContribution(recipeId)
            }
        }.onFailure {
            Log.e(TAG, "Error deleting recipe", it)
            Analytics.logNonFatalException(it)
        }
    }
}
