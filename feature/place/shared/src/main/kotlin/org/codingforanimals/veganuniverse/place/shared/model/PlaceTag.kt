package org.codingforanimals.veganuniverse.place.shared.model

import android.util.Log

enum class PlaceTag {
    GLUTEN_FREE,
    FULL_VEGAN,
    DELIVERY,
    TAKEAWAY,
    DINE_IN,
    ;

    companion object {
        private const val TAG = "PlaceTag"
        fun fromString(value: String?): PlaceTag? {
            return runCatching {
                value?.let { PlaceTag.valueOf(it) }
            }.onFailure {
                Log.i(TAG, it.message ?: it.stackTraceToString())
            }.getOrNull()
        }
    }
}