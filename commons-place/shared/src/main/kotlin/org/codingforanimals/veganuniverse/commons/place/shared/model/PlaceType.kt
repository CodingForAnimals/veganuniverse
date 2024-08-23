package org.codingforanimals.veganuniverse.commons.place.shared.model

import android.util.Log

enum class PlaceType {
    STORE,
    RESTAURANT,
    CAFE,
    BAR,
    ;

    companion object {
        private const val TAG = "PlaceType"
        fun fromString(value: String?): PlaceType? {
            return runCatching {
                value?.let { PlaceType.valueOf(it) }
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }.getOrNull()
        }
    }
}