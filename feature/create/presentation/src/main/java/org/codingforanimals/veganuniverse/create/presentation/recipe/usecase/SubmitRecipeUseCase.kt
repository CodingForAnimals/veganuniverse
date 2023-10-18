package org.codingforanimals.veganuniverse.create.presentation.recipe.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.model.User
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.create.domain.recipes.RecipeCreator
import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm

private const val TAG = "SubmitRecipeUseCase"

internal class SubmitRecipeUseCase(
    private val getUserStatus: GetUserStatus,
    private val recipeCreator: RecipeCreator,
) {
    operator fun invoke(uiState: CreateRecipeViewModel.UiState): Flow<SubmitRecipeStatus> = flow {
        val user = getUserStatus().value ?: return@flow emit(SubmitRecipeStatus.UnauthorizedUser)

        if (!user.isEmailVerified) {
            emit(SubmitRecipeStatus.Loading)
            val refreshUser =
                getUserStatus.refreshUser() ?: return@flow emit(SubmitRecipeStatus.UnauthorizedUser)
            if (!refreshUser.isEmailVerified) {
                return@flow emit(SubmitRecipeStatus.EmailNotVerified)
            }
        }

        emit(SubmitRecipeStatus.Loading)
        try {
            val recipeForm = uiState.toRecipeForm(user)
            recipeCreator.createRecipe(recipeForm)
            emit(SubmitRecipeStatus.Success)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(SubmitRecipeStatus.Error)
        }
    }

    private fun CreateRecipeViewModel.UiState.toRecipeForm(user: User): RecipeForm {
        return RecipeForm(
            userId = user.id,
            username = user.name,
            image = pictureField.model!!,
            title = titleField.value,
            description = descriptionField.value,
            tags = tagsField.tags.filter { it.selected }.map { it.name },
            ingredients = ingredientsField.list.filter { it.isNotBlank() },
            steps = stepsField.list.filter { it.isNotBlank() },
            prepTime = prepTimeField.value,
            servings = servingsField.value,
        )
    }
}