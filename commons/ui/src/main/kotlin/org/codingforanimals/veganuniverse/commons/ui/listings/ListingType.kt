package org.codingforanimals.veganuniverse.commons.ui.listings

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
                Log.i(TAG, it.message ?: it.stackTraceToString())
            }.getOrNull()
        }
    }
}