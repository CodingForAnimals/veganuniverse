package org.codingforanimals.veganuniverse.place.shared.model

import android.util.Log

enum class PlaceCardSorter {
    NAME,
    RATING,
    ;

    companion object {
        private const val TAG = "PlaceCardSorter"
        fun fromString(value: String?): PlaceCardSorter? {
            return runCatching {
                value?.let { PlaceCardSorter.valueOf(it) }
            }.onFailure {
                Log.i(TAG, it.message ?: it.stackTraceToString())
            }.getOrNull()
        }
    }
}