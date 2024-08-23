package org.codingforanimals.veganuniverse.commons.recipe.data.remote.mapper

import org.codingforanimals.veganuniverse.commons.recipe.data.remote.entity.RecipeFirestoreEntity
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe

internal interface RecipeFirestoreEntityMapper {
    fun mapToDataModel(firestoreEntity: RecipeFirestoreEntity): Recipe
}
