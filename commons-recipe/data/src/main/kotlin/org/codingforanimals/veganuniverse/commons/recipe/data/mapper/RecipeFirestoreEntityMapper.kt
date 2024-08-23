package org.codingforanimals.veganuniverse.commons.recipe.data.mapper

import org.codingforanimals.veganuniverse.commons.recipe.data.model.RecipeFirestoreEntity
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe

internal interface RecipeFirestoreEntityMapper {
    fun mapToDataModel(firestoreEntity: RecipeFirestoreEntity): Recipe
}
