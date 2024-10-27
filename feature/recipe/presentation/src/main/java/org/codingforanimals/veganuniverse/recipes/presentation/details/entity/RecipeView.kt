package org.codingforanimals.veganuniverse.recipes.presentation.details.entity

import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.model.RecipeTagUI
import java.util.Date

data class RecipeView(
    val id: String,
    val userId: String?,
    val username: String?,
    val name: String,
    val description: String,
    val likes: Int,
    val createdAt: Date?,
    val tags: List<RecipeTagUI>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
    val imageUrl: String?,
) {
    val likesText = when (likes) {
        0 -> R.string.recipe_no_favs
        1 -> R.string.recipe_one_favs
        else -> R.string.recipe_n_favs
    }
}
