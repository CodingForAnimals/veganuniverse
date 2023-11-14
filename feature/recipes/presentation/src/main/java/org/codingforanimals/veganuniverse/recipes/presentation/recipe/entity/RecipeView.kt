package org.codingforanimals.veganuniverse.recipes.presentation.recipe.entity

import java.util.Date

data class RecipeView(
    val id: String,
    val userId: String,
    val username: String,
    val title: String,
    val description: String,
    val likes: Int,
    val createdAt: Date,
    val tags: List<org.codingforanimals.veganuniverse.recipes.ui.RecipeTag>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
    val imageRef: String,
)
