package org.codingforanimals.veganuniverse.recipes.presentation.details.model

sealed class RecipeToggleableItem {
    data object Like : RecipeToggleableItem()
    data object Bookmark : RecipeToggleableItem()
}