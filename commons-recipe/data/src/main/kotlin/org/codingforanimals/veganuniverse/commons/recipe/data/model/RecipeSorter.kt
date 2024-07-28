package org.codingforanimals.veganuniverse.commons.recipe.data.model

import com.google.firebase.firestore.Query.Direction
import org.codingforanimals.veganuniverse.commons.recipe.data.RecipeFirestoreDataSource
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeSorter

internal val RecipeSorter.getSortingField: String
    get() = when (this) {
        RecipeSorter.DATE -> RecipeFirestoreDataSource.FIELD_CREATED_AT
        RecipeSorter.LIKES -> RecipeFirestoreDataSource.FIELD_LIKES
        RecipeSorter.NAME -> RecipeFirestoreDataSource.FIELD_NAME_LOWERCASE
    }

internal val RecipeSorter.getSortingDirection: Direction
    get() = when (this){
        RecipeSorter.NAME -> Direction.ASCENDING
        RecipeSorter.DATE -> Direction.DESCENDING
        RecipeSorter.LIKES -> Direction.DESCENDING
    }
