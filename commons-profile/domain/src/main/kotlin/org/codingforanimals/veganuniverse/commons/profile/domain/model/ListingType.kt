package org.codingforanimals.veganuniverse.commons.profile.domain.model

import android.util.Log

enum class ListingType {
    CONTRIBUTIONS,
    BOOKMARKS,
    ;
    companion object {
        private const val TAG = "ListingType"
        fun fromString(value: String?): ListingType? {
            return runCatching {
                value?.let { ListingType.valueOf(it) }
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }.getOrNull()
        }
    }
}