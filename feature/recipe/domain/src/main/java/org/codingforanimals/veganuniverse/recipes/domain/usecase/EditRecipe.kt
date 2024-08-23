package org.codingforanimals.veganuniverse.recipes.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class EditRecipe(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(recipeId: String, edition: String): Result {
        val user = flowOnCurrentUser().firstOrNull()
            ?: return Result.UnauthenticatedUser

        return try {
            recipeRepository.editRecipe(recipeId, user.id, edition)
            Result.Success
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Analytics.logNonFatalException(e)
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnauthenticatedUser : Result()
        data object UnexpectedError : Result()
        data object Success : Result()
    }

    companion object {
        private const val TAG = "EditRecipe"
    }
}