package org.codingforanimals.veganuniverse.create.recipe.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.network.PermissionDeniedException
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.create.recipe.domain.model.RecipeForm

class SubmitRecipe(
    private val recipeRepository: RecipeRepository,
    private val profileContentUseCases: ProfileContentUseCases,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(recipeForm: RecipeForm): Result {
        val user = flowOnCurrentUser(true).firstOrNull() ?: return Result.GuestUser
        if (!user.isEmailVerified) {
            return Result.UnverifiedEmail
        }

        val recipeFormAsModel = recipeForm.toModel(user.id, user.name)
        return try {
            val recipe = recipeRepository.insertRecipe(recipeFormAsModel, recipeForm.imageModel)
                ?.also { finalRecipe ->
                    finalRecipe.id?.let { profileContentUseCases.addContribution(it) }
                }
            return recipe?.let {
                Result.Success(it)
            } ?: Result.UnexpectedError
        } catch (e: PermissionDeniedException) {
            Result.UserMustReauthenticate
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Result.UnexpectedError
        }
    }

    private fun RecipeForm.toModel(userId: String, username: String): Recipe {
        return Recipe(
            id = null,
            userId = userId,
            username = username,
            title = title,
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

    sealed class Result {
        data object GuestUser : Result()
        data object UnexpectedError : Result()
        data object UserMustReauthenticate : Result()
        data object UnverifiedEmail : Result()
        data class Success(val recipe: Recipe) : Result()
    }

    companion object {
        private const val TAG = "SubmitRecipe"
    }
}
