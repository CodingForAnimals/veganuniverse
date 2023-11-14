package org.codingforanimals.veganuniverse.recipes.storage

interface RecipeListCache {
    fun appendRecipes(key: String, recipesIds: List<String>): Boolean
    fun getRecipesIds(key: String): List<String>?
}