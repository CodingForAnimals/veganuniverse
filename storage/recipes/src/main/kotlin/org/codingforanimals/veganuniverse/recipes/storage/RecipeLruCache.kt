package org.codingforanimals.veganuniverse.recipes.storage

import android.util.Log
import android.util.LruCache
import org.codingforanimals.veganuniverse.recipes.entity.Recipe

private const val TAG = "RecipeLruCache"

internal class RecipeLruCache : LruCache<String, Recipe>(4 * 1024 * 1024), RecipeCache {
    override fun putRecipe(recipe: Recipe): Boolean {
        return try {
            put(recipe.id, recipe)
            true
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            false
        }
    }

    override fun getRecipe(id: String): Recipe? {
        return try {
            get(id)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

}