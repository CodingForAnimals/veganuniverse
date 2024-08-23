package org.codingforanimals.veganuniverse.commons.place.domain.model

import android.util.Log


enum class DayOfWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    companion object {
        private const val TAG = "DayOfWeek"
        fun fromString(value: String?): DayOfWeek? {
            return runCatching { value?.let { DayOfWeek.valueOf(it) } }
                .onFailure { Log.e(TAG, it.stackTraceToString()) }
                .getOrNull()
        }
    }
}
