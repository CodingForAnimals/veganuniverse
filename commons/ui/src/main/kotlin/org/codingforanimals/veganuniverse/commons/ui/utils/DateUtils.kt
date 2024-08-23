package org.codingforanimals.veganuniverse.commons.ui.utils

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import org.codingforanimals.veganuniverse.commons.ui.R
import java.util.Date

object DateUtils {

    /**
     * Ej. '3 hours ago' (hace 3 horas)
     * Ej. 'Apr 27 2024' (27 abr 2024)
     * Ej. If time difference is less than one hour, 'ahora'
     */
    @Composable
    fun getTimeAgo(time: Long): String {
        val context = LocalContext.current
        return rememberSaveable {
            runCatching {
                val now = Date().time
                if (time - ONE_HOUR > now) {
                    DateUtils.getRelativeTimeSpanString(
                        /* time = */ time,
                        /* now = */ Date().time,
                        /* minResolution = */ DateUtils.HOUR_IN_MILLIS
                    ).toString()
                } else {
                    context.getString(R.string.now).lowercase()
                }
            }.getOrElse {
                Log.e(TAG, it.stackTraceToString())
                it.stackTraceToString()
            }
        }
    }

    private const val TAG = "DateUtils"
    private const val EMPTY_STRING = "asd"
    private const val ONE_HOUR = 1000 * 60 * 60 // 1000 millis times 60 seconds times 60 minutes
}
