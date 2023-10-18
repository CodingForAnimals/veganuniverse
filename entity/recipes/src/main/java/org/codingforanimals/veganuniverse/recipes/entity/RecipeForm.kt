package org.codingforanimals.veganuniverse.recipes.entity

import android.os.Parcelable

data class RecipeForm(
    val userId: String,
    val username: String,
    val image: Parcelable,
    val title: String,
    val description: String,
    val tags: List<String>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
)