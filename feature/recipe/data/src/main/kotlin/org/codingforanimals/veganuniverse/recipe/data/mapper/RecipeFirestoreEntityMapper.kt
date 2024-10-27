package org.codingforanimals.veganuniverse.recipe.data.mapper

import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.recipe.data.model.RecipeFirestoreEntity


internal interface RecipeFirestoreEntityMapper {
    fun mapToDataModel(firestoreEntity: RecipeFirestoreEntity): Recipe
}
