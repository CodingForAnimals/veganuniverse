package org.codingforanimals.veganuniverse.recipes.domain

import org.codingforanimals.veganuniverse.recipes.entity.Recipe

interface RecipeListCache {
    fun appendRecipes(key: String, recipes: List<Recipe>): Boolean
    fun getRecipes(key: String): List<Recipe>?

    companion object {
        const val MOST_LIKED = "recipes_cache_key_most_liked"
        const val MOST_RECENT = "recipes_cache_key_most_recent"
    }
}