package org.codingforanimals.veganuniverse.create.recipe.domain.usecase

import org.codingforanimals.veganuniverse.create.recipe.domain.model.RecipeForm
import org.codingforanimals.veganuniverse.network.NetworkUtils
import org.codingforanimals.veganuniverse.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.recipe.model.Recipe
import org.codingforanimals.veganuniverse.user.domain.repository.CurrentUserRepository

class SubmitRecipe(
    private val currentUserRepository: CurrentUserRepository,
    private val recipeRepository: RecipeRepository,
    private val profileContentUseCases: ProfileContentUseCases,
    private val networkUtils: NetworkUtils,
) {
    suspend operator fun invoke(recipeForm: RecipeForm): Result {
        if (!networkUtils.isNetworkAvailable()) return Result.NoInternet
        var user = currentUserRepository.getCurrentUser() ?: return Result.GuestUser
        if (!user.isEmailVerified) {
            val refreshedUser = currentUserRepository.refreshUser() ?: return Result.GuestUser
            refreshedUser.takeIf { it.isEmailVerified }?.let { user = it }
                ?: return Result.UnverifiedEmail
        }
        val recipeFormAsModel = recipeForm.toModel(user.id, user.name)
        val recipe = recipeRepository.insertRecipe(recipeFormAsModel, recipeForm.imageModel)
            ?.also { finalRecipe ->
                finalRecipe.id?.let { profileContentUseCases.addContribution(it) }
            }
        return recipe?.let {
            Result.Success(it)
        } ?: Result.UnexpectedError
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
        data object NoInternet : Result()
        data object GuestUser : Result()
        data object UnexpectedError : Result()
        data object UnverifiedEmail : Result()
        data class Success(val recipe: Recipe) : Result()
    }
}
