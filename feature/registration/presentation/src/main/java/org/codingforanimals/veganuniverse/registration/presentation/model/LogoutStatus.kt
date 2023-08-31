package org.codingforanimals.veganuniverse.registration.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.registration.presentation.R

sealed class LogoutStatus {
    data object Loading : LogoutStatus()
    data object Success : LogoutStatus()
    sealed class Exception(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : LogoutStatus() {
        data object UnknownException : Exception(
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )
    }
}