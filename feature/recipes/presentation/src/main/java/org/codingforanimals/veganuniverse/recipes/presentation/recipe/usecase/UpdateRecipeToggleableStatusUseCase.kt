package org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.model.RecipeToggleableItem

private const val TAG = "UpdateRecipeToggleableS"

class UpdateRecipeToggleableStatusUseCase(
    private val recipesRepository: RecipesRepository,
    private val getUserStatus: GetUserStatus,
) {
    suspend operator fun invoke(
        currentToggleState: Boolean,
        recipeId: String,
        item: RecipeToggleableItem,
    ): Flow<Status> = flow {
        val result = try {
            getUserStatus().value?.id?.let { userId ->
                emit(Status.Loading)
                val toggled = when (item) {
                    RecipeToggleableItem.Bookmark -> recipesRepository.updateBookmarkReturningCurrent(
                        bookmark = !currentToggleState,
                        recipeId = recipeId,
                        userId = userId,
                    )

                    RecipeToggleableItem.Like -> recipesRepository.updateLikeReturningCurrent(
                        like = !currentToggleState,
                        recipeId = recipeId,
                        userId = userId,
                    )
                }
                Status.Success(toggled)
            } ?: Status.GuestUser
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Status.Error
        }
        emit(result)
    }

    sealed class Status {
        data object Loading : Status()
        data object GuestUser : Status()
        data object Error : Status()
        data class Success(val toggled: Boolean) : Status()
    }
}