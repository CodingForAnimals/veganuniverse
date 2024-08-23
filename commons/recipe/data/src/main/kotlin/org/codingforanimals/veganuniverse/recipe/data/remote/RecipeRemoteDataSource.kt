package org.codingforanimals.veganuniverse.recipe.data.remote

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.recipe.model.Recipe
import org.codingforanimals.veganuniverse.recipe.model.RecipeQueryParams

interface RecipeRemoteDataSource {
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun getRecipesByIdList(ids: List<String>): List<Recipe>
    fun queryRecipesPagingData(params: RecipeQueryParams): Flow<PagingData<Recipe>>
    suspend fun getRecipeByQueryParams(params: RecipeQueryParams): List<Recipe>
    suspend fun insertRecipe(recipe: Recipe, model: Parcelable): Recipe?
    suspend fun deleteRecipeById(id: String): Boolean
    suspend fun increaseOrDecreaseLike(recipeId: String, shouldIncrease: Boolean)
}
