package org.codingforanimals.veganuniverse.recipes.storage

import android.util.Log
import android.util.LruCache

private const val TAG = "RecipeListLruCache"

internal class RecipeListLruCache :
    LruCache<String, List<String>>(4 * 1024 * 1024), RecipeListCache {

    override fun appendRecipes(key: String, recipesIds: List<String>): Boolean {
        return try {
            val current = (get(key) ?: emptyList()).toMutableList()
            current.addAll(recipesIds)
            put(key, current)
            true
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            false
        }
    }

    override fun getRecipesIds(key: String): List<String>? {
        return try {
            get(key)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }
}