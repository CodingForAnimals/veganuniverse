package org.codingforanimals.veganuniverse.commons.recipe.shared.model

import java.util.Date

data class Recipe(
    val id: String?,
    val userId: String?,
    val username: String?,
    val title: String,
    val description: String,
    val likes: Int,
    val createdAt: Date?,
    val tags: List<RecipeTag>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
    val imageUrl: String?,
    val validated: Boolean,
)