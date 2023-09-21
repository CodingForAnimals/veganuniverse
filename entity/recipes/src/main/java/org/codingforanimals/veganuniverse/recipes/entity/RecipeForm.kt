package org.codingforanimals.veganuniverse.recipes.entity

data class RecipeForm(
    val userId: String,
    val title: String,
    val category: String,
    val description: String,
    val likes: Int,
    val tags: List<String>,
    val ingredients: List<String>,
    val steps: List<Step>,
    val prepTime: PrepTime,
)

data class Step(
    val description: String,
    val order: Int,
)

data class PrepTime(
    val hours: Int,
    val minutes: Int,
    val isQuickRecipe: Boolean,
)