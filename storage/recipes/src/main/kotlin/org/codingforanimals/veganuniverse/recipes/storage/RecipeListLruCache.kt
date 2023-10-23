package org.codingforanimals.veganuniverse.recipes.storage

import android.util.Log
import android.util.LruCache
import org.codingforanimals.veganuniverse.recipes.entity.Recipe

private const val TAG = "RecipeListLruCache"

internal class RecipeListLruCache :
    LruCache<String, List<Recipe>>(4 * 1024 * 1024), RecipeListCache {

    override fun appendRecipes(key: String, recipes: List<Recipe>): Boolean {
        return try {
            val current = (get(key) ?: emptyList()).toMutableList()
            current.addAll(recipes)
            put(key, current)
            true
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            false
        }
    }

    override fun getRecipes(key: String): List<Recipe>? {
        return try {
            get(key)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }
}