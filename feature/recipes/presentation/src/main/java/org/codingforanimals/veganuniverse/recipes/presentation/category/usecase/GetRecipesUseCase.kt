package org.codingforanimals.veganuniverse.recipes.presentation.category.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams
import org.codingforanimals.veganuniverse.recipes.presentation.category.model.GetRecipesStatus
import org.codingforanimals.veganuniverse.recipes.presentation.entity.toDomainSorter
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag

private const val TAG = "GetRecipesUseCase"

internal class GetRecipesUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val recipesRepository: RecipesRepository,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    operator fun invoke(
        recipes: List<Recipe>,
        filterTag: RecipeTag?,
        sorter: RecipeSorter,
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

