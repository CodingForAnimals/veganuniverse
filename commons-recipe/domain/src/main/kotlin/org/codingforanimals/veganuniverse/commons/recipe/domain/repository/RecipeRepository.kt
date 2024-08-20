package org.codingforanimals.veganuniverse.commons.recipe.domain.repository

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeQueryParams

interface RecipeRepository {
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun queryRecipesById(ids: List<String>): Flow<PagingData<Recipe>>
    fun queryRecipesPagingData(params: RecipeQueryParams): Flow<PagingData<Recipe>>
    suspend fun queryRecipes(params: RecipeQueryParams): List<Recipe>
    suspend fun deleteRecipeById(id: String)
    suspend fun insertRecipe(recipe: Recipe, imageModel: Parcelable): Recipe
    suspend fun increaseOrDecreaseLike(recipeId: String, shouldIncrease: Boolean)
    suspend fun reportRecipe(recipeId: String, userId: String)
    suspend fun editRecipe(recipeId: String, userId: String, suggestion: String)
}
