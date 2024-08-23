package org.codingforanimals.veganuniverse.commons.recipe.shared.model

import android.util.Log

enum class RecipeSorter {
    NAME,
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