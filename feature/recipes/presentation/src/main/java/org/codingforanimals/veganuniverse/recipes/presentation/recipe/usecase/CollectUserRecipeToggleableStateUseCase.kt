package org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.model.RecipeToggleableItem

private const val TAG = "CollectUserRecipeToggle"

class CollectUserRecipeToggleableStateUseCase(
    private val recipesRepository: RecipesRepository,
    private val getUserStatus: GetUserStatus,
) {
    suspend operator fun invoke(
        recipeId: String,
        item: RecipeToggleableItem,
    ): Flow<Status> = getUserStatus().transform { user ->
        if (user == null) return@transform emit(Status.Success(false))
        emit(Status.Loading)
        try {
            val toggled = when (item) {
                RecipeToggleableItem.Bookmark -> recipesRepository.isRecipeBookmarkedByUser(
                    recipeId = recipeId,
                    userId = user.id
                )

                RecipeToggleableItem.Like -> recipesRepository.isRecipeLikedByUser(
                    recipeId = recipeId,
                    userId = user.id
                )
            }
            emit(Status.Success(toggled))
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(Status.Error)
        }
    }

    sealed class Status {
        data object Loading : Status()
        data object Error : Status()
        data class Success(val toggled: Boolean) : Status()
    }
}

