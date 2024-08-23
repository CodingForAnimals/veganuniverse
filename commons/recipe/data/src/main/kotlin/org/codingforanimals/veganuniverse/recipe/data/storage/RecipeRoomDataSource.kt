package org.codingforanimals.veganuniverse.recipe.data.storage

import org.codingforanimals.veganuniverse.recipe.data.storage.database.RecipeRoomDao
import org.codingforanimals.veganuniverse.recipe.data.storage.model.RecipeRoomEntityMapper
import org.codingforanimals.veganuniverse.recipe.model.Recipe

internal class RecipeRoomDataSource(
    private val dao: RecipeRoomDao,
    private val mapper: RecipeRoomEntityMapper,
) : RecipeLocalDataSource {
    override suspend fun getRecipeById(id: String): Recipe? {
        return dao.getById(id)?.let { mapper.mapToModel(it) }
    }

    override suspend fun getRecipesByIdList(ids: List<String>): List<Recipe> {
        return dao.getByIdList(ids).map { entity -> mapper.mapToModel(entity) }
    }

    override suspend fun insertRecipe(vararg recipe: Recipe) {
        dao.insertRecipe(*recipe.mapNotNull { mapper.mapToEntity(it) }.toTypedArray())
    }

    override suspend fun deleteRecipeById(id: String): Boolean {
        val deletedCount = dao.deleteById(id)
        return deletedCount > 0
    }
}
