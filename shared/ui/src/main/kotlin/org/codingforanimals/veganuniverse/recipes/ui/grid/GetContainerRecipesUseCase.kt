package org.codingforanimals.veganuniverse.recipes.ui.grid

import android.util.Log
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag
import org.codingforanimals.veganuniverse.shared.ui.grid.model.StaggeredItem

internal class GetContainerRecipesUseCase(
    private val recipesRepository: org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository,
) {
    suspend operator fun invoke(tag: RecipeTag): Status {
        return try {
            val params = org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams(
                tag = tag.name,
                limit = 3,
                sorter = org.codingforanimals.veganuniverse.recipes.entity.RecipeSorter.LIKES,
            )
            val recipes = recipesRepository.getCachedRecipes(tag.name)
                ?: recipesRepository.fetchRecipes(params, tag.name)
            Status.Success(recipes)
        } catch (e: Throwable) {
            Log.e(
                org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.usecase.TAG,
                e.stackTraceToString()
            )
            Status.Error
        }
    }

    sealed class Status {
        data object Loading : Status()
        data object Error : Status()
        data class Success(
            private val recipes: List<org.codingforanimals.veganuniverse.recipes.entity.Recipe>,
        ) : Status() {
            val items = recipes.map {
                StaggeredItem(id = it.id, title = it.title, imageRef = it.imageRef)
            }
        }
    }
}