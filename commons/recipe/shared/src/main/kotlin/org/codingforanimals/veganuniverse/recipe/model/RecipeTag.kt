package org.codingforanimals.veganuniverse.recipe.model

import android.util.Log

enum class RecipeTag {
    GLUTEN_FREE,
    LOW_SODIUM,
    SUGAR_FREE,
    NOT_DAIRY,
    LUNCH_AND_DINNER,
    SALTY,
    NIBBLES,
    BREAKFAST_AND_EVENING,
    QUICK_RECIPE,
    SWEET,
    DRESSING,
    CANNED,
    ;

    companion object {
        private const val TAG = "RecipeTag"
        fun fromString(value: String?): RecipeTag? {
            return runCatching { value?.let { RecipeTag.valueOf(it) } }
                .onFailure { Log.w(TAG, it.stackTraceToString()) }
                .getOrNull()
        }
    }
}
