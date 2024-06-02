package org.codingforanimals.veganuniverse.recipe.data.storage

import org.codingforanimals.veganuniverse.recipe.model.Recipe

interface RecipeLocalDataSource {
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun getRecipesByIdList(ids: List<String>): List<Recipe>
    suspend fun insertRecipe(vararg recipe: Recipe)
    suspend fun deleteRecipeById(id: String): Boolean
}
