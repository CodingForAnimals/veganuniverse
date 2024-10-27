package org.codingforanimals.veganuniverse.commons.analytics

import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

object Analytics {
    fun logNonFatalException(exception: Throwable) {
        Log.e("Non-Fatal Exception", exception.message.orEmpty(), exception)
        Firebase.crashlytics.recordException(exception)
        Firebase.crashlytics.log(exception.message ?: "Non-Fatal Exception")
    }

    fun logUserEvent(name: String, data: Map<String, String>) {
        Firebase.analytics.logEvent(name) {
            data.forEach { (key, value) ->
                param(key, value)
            }
        }
    }
}
