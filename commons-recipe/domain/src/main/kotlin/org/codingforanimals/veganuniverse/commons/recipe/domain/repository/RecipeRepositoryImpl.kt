package org.codingforanimals.veganuniverse.commons.recipe.domain.repository

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.RecipeRemoteDataSource
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeQueryParams

internal class RecipeRepositoryImpl(
    private val remoteDataSource: RecipeRemoteDataSource,
) : RecipeRepository {
    override suspend fun getRecipeById(id: String): Recipe? {
        return remoteDataSource.getRecipeById(id)
    }

    override suspend fun getRecipesByIdList(ids: List<String>): List<Recipe> {
        return ids.mapNotNull { getRecipeById(it) }
    }

    override fun queryRecipesPagingData(params: RecipeQueryParams): Flow<PagingData<Recipe>> {
        return remoteDataSource.queryRecipesPagingData(params)
    }

    override suspend fun queryRecipes(params: RecipeQueryParams): List<Recipe> {
        return remoteDataSource.getRecipeByQueryParams(params)
    }

    override suspend fun deleteRecipeById(id: String): Boolean {
        return remoteDataSource.deleteRecipeById(id)
    }

    override suspend fun insertRecipe(recipe: Recipe, imageModel: Parcelable): Recipe? {
        return remoteDataSource.insertRecipe(recipe, imageModel)
    }

    override suspend fun increaseOrDecreaseLike(recipeId: String, shouldIncrease: Boolean) {
        remoteDataSource.increaseOrDecreaseLike(recipeId, shouldIncrease)
    }

    override suspend fun reportRecipe(recipeId: String, userId: String) {
        remoteDataSource.reportRecipe(recipeId, userId)
    }

    override suspend fun editRecipe(recipeId: String, userId: String, suggestion: String) {
        remoteDataSource.editRecipe(recipeId, userId, suggestion)
    }
}
