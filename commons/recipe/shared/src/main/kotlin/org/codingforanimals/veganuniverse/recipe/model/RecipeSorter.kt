package org.codingforanimals.veganuniverse.recipe.model

import android.util.Log

enum class RecipeSorter {
    DATE,
    LIKES,
    ;

    companion object {
        private const val TAG = "RecipeSorter"
        fun fromString(value: String?): RecipeSorter? {
            return runCatching { value?.let { RecipeSorter.valueOf(it) } }
                .onFailure { Log.w(TAG, it.stackTraceToString()) }
                .getOrNull()
        }
    }
}
