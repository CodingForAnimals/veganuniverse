package org.codingforanimals.veganuniverse.recipe.data.remote.mapper

import org.codingforanimals.veganuniverse.recipe.data.remote.entity.RecipeFirestoreEntity
import org.codingforanimals.veganuniverse.recipe.model.Recipe

internal interface RecipeFirestoreEntityMapper {
    fun mapToDataModel(firestoreEntity: RecipeFirestoreEntity): Recipe
}
