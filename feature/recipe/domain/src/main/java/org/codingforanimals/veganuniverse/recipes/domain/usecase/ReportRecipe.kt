package org.codingforanimals.veganuniverse.recipes.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.network.PermissionDeniedException
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class ReportRecipe(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(recipeId: String): Result {
        val user = flowOnCurrentUser().firstOrNull()
            ?: return Result.UnauthenticatedUser

        return try {
            recipeRepository.reportRecipe(recipeId, user.id)
            Result.Success
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnauthenticatedUser : Result()
        data object UnexpectedError : Result()
        data object Success : Result()
    }

    companion object {
        private const val TAG = "ReportRecipe"
    }
}