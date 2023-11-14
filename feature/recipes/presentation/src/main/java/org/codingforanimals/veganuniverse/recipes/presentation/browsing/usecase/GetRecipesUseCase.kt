package org.codingforanimals.veganuniverse.recipes.presentation.browsing.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.model.GetRecipesStatus
import org.codingforanimals.veganuniverse.recipes.presentation.entity.toDomainSorter

private const val TAG = "GetRecipesUseCase"

internal class GetRecipesUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val recipesRepository: RecipesRepository,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    operator fun invoke(
        recipes: List<Recipe>,
        filterTag: org.codingforanimals.veganuniverse.recipes.ui.RecipeTag?,
        sorter: org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter,
    ): Flow<GetRecipesStatus> =
        flow {
            emit(GetRecipesStatus.Loading)
            try {
                val newRecipes = withContext(ioDispatcher) {
                    val params = RecipeQueryParams(
                        lastRecipe = recipes.lastOrNull(),
                        tag = filterTag?.name,
                        sorter = sorter.toDomainSorter(),
                    )
                    recipesRepository.fetchRecipes(params)
                }
                emit(GetRecipesStatus.Success(newRecipes))
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                emit(GetRecipesStatus.Error)
            }
        }
}

