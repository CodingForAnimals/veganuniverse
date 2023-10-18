package org.codingforanimals.veganuniverse.recipes.presentation.entity

import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter
import org.codingforanimals.veganuniverse.recipes.entity.RecipeSorter as DomainRecipeSorter

internal fun RecipeSorter.toDomainSorter(): DomainRecipeSorter {
    return when (this) {
        RecipeSorter.DATE -> DomainRecipeSorter.DATE
        RecipeSorter.LIKES -> DomainRecipeSorter.LIKES
    }
}