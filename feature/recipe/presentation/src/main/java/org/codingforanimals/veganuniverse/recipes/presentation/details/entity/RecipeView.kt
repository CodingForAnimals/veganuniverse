package org.codingforanimals.veganuniverse.recipes.presentation.details.entity

import org.codingforanimals.veganuniverse.recipe.presentation.RecipeTagUI
import java.util.Date

data class RecipeView(
    val id: String,
    val userId: String?,
    val username: String?,
    val title: String,
    val description: String,
    val likes: Int,
    val createdAt: Date?,
    val tags: List<RecipeTagUI>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
    val imageUrl: String?,
)
