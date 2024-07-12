package org.codingforanimals.veganuniverse.create.recipe.domain.model

import android.os.Parcelable
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag

data class RecipeForm(
    val imageModel: Parcelable,
    val name: String,
    val description: String,
    val tags: List<RecipeTag>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
)
