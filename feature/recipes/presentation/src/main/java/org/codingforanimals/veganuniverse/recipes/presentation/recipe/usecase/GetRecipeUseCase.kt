package org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.entity.RecipeView
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag

private const val TAG = "GetRecipeUseCase"

internal class GetRecipeUseCase(
    private val recipesRepository: RecipesRepository,
) {

    suspend operator fun invoke(id: String): Status {
        return try {
            val recipe = recipesRepository.getCachedRecipe(id)
                ?: recipesRepository.fetchRecipe(id)
            Status.Success(recipe!!.toView())
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
            imageRef = imageRef
        )
    }

    sealed class Status {
        data object Loading : Status()
        data object Error : Status()
        data class Success(val recipe: RecipeView) : Status()
    }
}