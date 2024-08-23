package org.codingforanimals.veganuniverse.place.model

import android.util.Log

enum class PlaceSorter {
    NAME,
    RATING,
    REVIEWS,
    DATE,
    ;

    companion object {
        private const val TAG = "PlaceSorter"
        fun fromString(value: String?): PlaceSorter? {
            return runCatching {
                value?.let { PlaceSorter.valueOf(it) }
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }.getOrNull()
        }
    }
}
