package org.codingforanimals.veganuniverse.recipe.data.source

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeQueryParams

interface RecipeRemoteDataSource {
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun queryRecipesPagingDataByIds(ids: List<String>): Flow<PagingData<Recipe>>
    fun queryRecipesPagingData(params: RecipeQueryParams): Flow<PagingData<Recipe>>
    suspend fun getRecipeByQueryParams(params: RecipeQueryParams): List<Recipe>
    suspend fun insertRecipe(recipe: Recipe, model: Parcelable): String
    suspend fun deleteRecipeById(id: String): Boolean
    suspend fun increaseOrDecreaseLike(recipeId: String, shouldIncrease: Boolean)
    suspend fun reportRecipe(recipeId: String, userId: String)
    suspend fun editRecipe(recipeId: String, userId: String, suggestion: String)
}
