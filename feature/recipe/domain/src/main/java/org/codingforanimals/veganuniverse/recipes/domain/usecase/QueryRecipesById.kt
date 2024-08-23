package org.codingforanimals.veganuniverse.recipes.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe

class QueryRecipesById(
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(ids: List<String>): Flow<PagingData<Recipe>> {
        return recipeRepository.queryRecipesById(ids)
    }
}