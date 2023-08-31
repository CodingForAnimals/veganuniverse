package org.codingforanimals.veganuniverse.registration.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.registration.presentation.R

sealed class EmailSignInStatus {
    data object Loading : EmailSignInStatus()
    data object Success : EmailSignInStatus()
    sealed class Exception(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : EmailSignInStatus() {

        data object UnknownException : Exception(
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )

        data object UserNotFound : Exception(
            title = R.string.sign_in_user_not_found_title,
            message = R.string.sign_in_user_not_found_message,
        )

        data object InvalidPassword : Exception(
            title = R.string.sign_in_invalid_password_title,
            message = R.string.sign_in_invalid_password_message,
        )

        data object InvalidUser : Exception(
            title = R.string.sign_in_invalid_user_title,
            message = R.string.sign_in_invalid_user_message,
        )

        data object ConnectionError : Exception(
            title = R.string.sign_in_connection_error_title,
            message = R.string.sign_in_connection_error_message,
        )
    }
}