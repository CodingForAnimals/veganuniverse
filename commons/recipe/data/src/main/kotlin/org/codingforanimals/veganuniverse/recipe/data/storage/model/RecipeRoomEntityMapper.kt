package org.codingforanimals.veganuniverse.recipe.data.storage.model

import org.codingforanimals.veganuniverse.recipe.data.storage.database.RecipeRoomEntity
import org.codingforanimals.veganuniverse.recipe.model.Recipe

internal interface RecipeRoomEntityMapper {
    fun mapToEntity(model: Recipe): RecipeRoomEntity?
    fun mapToModel(entity: RecipeRoomEntity): Recipe
}

