package org.codingforanimals.veganuniverse.registration.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.registration.presentation.R

sealed class LogoutStatus {
    object Loading : LogoutStatus()
    object Success : LogoutStatus()
    sealed class Exception(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : LogoutStatus() {
        object UnknownException : Exception(
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )
    }
}