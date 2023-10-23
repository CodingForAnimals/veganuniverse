package org.codingforanimals.veganuniverse.recipes.presentation.recipe.model

sealed class RecipeToggleableItem {
    data object Like : RecipeToggleableItem()
    data object Bookmark : RecipeToggleableItem()
}