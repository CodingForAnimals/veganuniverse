package org.codingforanimals.veganuniverse.commons.ui.utils

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import java.util.Date

object DateUtils {
    /**
     * Ej. '3 hours ago' (hace 3 horas)
     * Ej. 'Apr 27 2024' (27 abr 2024)
     * Ej. If time difference is less than one hour, 'ahora'
     */
    @Composable
    fun getTimeAgo(time: Long): String {
        return rememberSaveable {
            runCatching {
                DateUtils.getRelativeTimeSpanString(
                    /* time = */ time,
                    /* now = */ Date().time,
                    /* minResolution = */ DateUtils.MINUTE_IN_MILLIS
                ).toString().lowercase()
            }.getOrElse {
                Log.e(TAG, it.stackTraceToString())
                ""
            }
        }
    }

    private const val TAG = "DateUtils"
}
