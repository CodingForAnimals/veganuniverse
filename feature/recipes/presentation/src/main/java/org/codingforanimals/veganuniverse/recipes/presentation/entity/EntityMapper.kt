package org.codingforanimals.veganuniverse.recipes.presentation.entity

import org.codingforanimals.veganuniverse.recipes.entity.RecipeSorter as DomainRecipeSorter

internal fun org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter.toDomainSorter(): DomainRecipeSorter {
    return when (this) {
        org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter.DATE -> DomainRecipeSorter.DATE
        org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter.LIKES -> DomainRecipeSorter.LIKES
    }
}