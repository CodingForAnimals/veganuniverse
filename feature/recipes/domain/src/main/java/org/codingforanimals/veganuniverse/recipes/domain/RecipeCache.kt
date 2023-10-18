package org.codingforanimals.veganuniverse.recipes.domain

import org.codingforanimals.veganuniverse.recipes.entity.Recipe

interface RecipeCache {
    fun putRecipe(recipe: Recipe): Boolean
    fun getRecipe(id: String): Recipe?
}