package org.codingforanimals.veganuniverse.commons.recipe.data.model

import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeSorter

internal val RecipeSorter.getSortingField: String
    get() = when (this) {
        RecipeSorter.DATE -> CREATED_AT
        RecipeSorter.LIKES -> LIKES
    }

private const val LIKES = "likes"
private const val CREATED_AT = "createdAt"
