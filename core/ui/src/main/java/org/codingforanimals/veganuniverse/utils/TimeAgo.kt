package org.codingforanimals.veganuniverse.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import org.codingforanimals.veganuniverse.core.ui.R

object TimeAgo {
    private const val SECOND_MILLIS = 1000;
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    @Composable
    fun getTimeAgo(time: Long): String {
        val context = LocalContext.current
        return rememberSaveable {
            getTimeAgo(time, context) ?: ""
        }
    }

    fun getTimeAgo(_time: Long, context: Context): String? {
        val time = if (_time < 1000000000000L) {
            _time * 1000
        } else _time

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> context.getString(R.string.time_ago_just_now)
            diff < 2 * MINUTE_MILLIS -> context.getString(R.string.time_ago_a_minute_ago)
            diff < 50 * MINUTE_MILLIS -> context.getString(
                R.string.time_ago_minutes_ago,
                (diff / MINUTE_MILLIS).toString()
            )

            diff < 90 * MINUTE_MILLIS -> context.getString(R.string.time_ago_an_hour_ago)
            (diff / HOUR_MILLIS) == 1L -> context.getString(R.string.time_ago_an_hour_ago)
            diff < 24 * HOUR_MILLIS -> context.getString(
                R.string.time_ago_hours_ago,
                (diff / HOUR_MILLIS).toString()
            )

            diff < 48 * HOUR_MILLIS -> context.getString(R.string.time_ago_yesterday)
            else -> context.getString(
                R.string.time_ago_days_ago,
                (diff / DAY_MILLIS).toString()
            )
        }
    }
}