package org.codingforanimals.veganuniverse.recipes.storage

import org.codingforanimals.veganuniverse.recipes.entity.Recipe

interface RecipeListCache {
    fun appendRecipes(key: String, recipes: List<Recipe>): Boolean
    fun getRecipes(key: String): List<Recipe>?
}