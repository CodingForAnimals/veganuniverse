package org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.entity.RecipeSorter
import org.codingforanimals.veganuniverse.ui.grid.StaggeredItem

private const val TAG = "GetContainerRecipesUseC"

internal class GetContainerRecipesUseCase(
    private val recipesRepository: RecipesRepository,
) {
    suspend operator fun invoke(tag: org.codingforanimals.veganuniverse.recipes.ui.RecipeTag): Status {
        return try {
            val params = RecipeQueryParams(
                tag = tag.name,
                limit = 3,
                sorter = RecipeSorter.LIKES,
            )
            val recipes = recipesRepository.getRecipeList(tag.name)
                ?: recipesRepository.fetchRecipes(params, tag.name)
            Status.Success(
                recipes.map {
                    StaggeredItem(
                        id = it.id,
                        title = it.title,
                        imageRef = it.imageRef
                    )
                },
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Status.Error
        }
    }

    sealed class Status {
        data object Loading : Status()
        data object Error : Status()
        data class Success(
            val recipes: List<StaggeredItem>,
        ) : Status()
    }
}