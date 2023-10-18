package org.codingforanimals.veganuniverse.recipes.entity

data class RecipeQueryParams(
    val title: String? = null,
    val tag: String? = null,
    val lastRecipe: Recipe? = null,
    val sorter: RecipeSorter = RecipeSorter.DATE,
    val limit: Long = 3,
)