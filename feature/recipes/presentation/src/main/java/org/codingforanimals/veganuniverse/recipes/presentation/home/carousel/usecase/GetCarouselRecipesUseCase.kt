package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.usecase

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.codingforanimals.veganuniverse.recipes.domain.RecipeListCache
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.presentation.entity.toDomainSorter
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter

private const val TAG = "GetCarouselRecipesUseCa"

internal class GetCarouselRecipesUseCase(
    private val recipesRepository: RecipesRepository,
) {

    suspend operator fun invoke(
        recipes: List<Recipe>,
        sorter: RecipeSorter,
    ) = coroutineScope {
        val params = RecipeQueryParams(
            sorter = sorter.toDomainSorter(),
            lastRecipe = recipes.lastOrNull(),
            limit = CAROUSEL_QUERY_LIMIT,
        )
        async {
            try {
                val prioritizeCacheForFirstLoad = recipes.isEmpty()
                val result = if (prioritizeCacheForFirstLoad) {
                    recipesRepository.getCachedRecipes(RecipeListCache.MOST_LIKED)
                        ?: recipesRepository.fetchRecipes(params, RecipeListCache.MOST_LIKED)
                } else {
                    recipesRepository.fetchRecipes(params, RecipeListCache.MOST_LIKED)
                }
                result
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                emptyList()
            }
        }
    }

    companion object {
        const val CAROUSEL_QUERY_LIMIT = 2L
    }
}