package org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.entity.RecipeView
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag

private const val TAG = "GetRecipeUseCase"

internal class GetRecipeUseCase(
    private val recipesRepository: RecipesRepository,
    private val getUserStatus: GetUserStatus,
) {

    suspend operator fun invoke(id: String?): Status = coroutineScope {
        if (id == null || id == "null") return@coroutineScope Status.Error
        try {
            val recipe = async {
                recipesRepository.getCachedRecipe(id)
                    ?: recipesRepository.fetchRecipe(id)
            }
            val isLikedByUser = async {
                getUserStatus().firstOrNull()?.id?.let { userId ->
                    recipesRepository.isRecipeLikedByUser(id, userId)
                } ?: false
            }
            awaitAll(recipe, isLikedByUser)
            Status.Success(recipe.await()!!.toView())
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Status.Error
        }
    }

    private fun Recipe.toView(): RecipeView {
        return RecipeView(
            id = id,
            userId = userId,
            username = username,
            title = title,
            description = description,
            likes = likes,
            createdAt = createdAt,
            tags = tags.mapNotNull {
                try {
                    RecipeTag.valueOf(it)
                } catch (e: Throwable) {
                    null
                }
            },
            ingredients = ingredients,
            steps = steps,
            prepTime = prepTime,
            servings = servings,
            imageRef = imageRef,
        )
    }

    sealed class Status {
        data object Loading : Status()
        data object Error : Status()
        data class Success(val recipe: RecipeView) : Status()
    }
}