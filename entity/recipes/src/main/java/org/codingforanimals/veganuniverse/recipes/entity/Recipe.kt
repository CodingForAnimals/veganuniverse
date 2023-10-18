package org.codingforanimals.veganuniverse.recipes.entity

import java.util.Date

data class Recipe(
    val id: String,
    val userId: String,
    val username: String,
    val title: String,
    val description: String,
    val likes: Int,
    val createdAt: Date,
    val tags: List<String>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
    val imageRef: String,
)