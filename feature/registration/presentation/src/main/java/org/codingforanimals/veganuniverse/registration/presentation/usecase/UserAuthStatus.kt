package org.codingforanimals.veganuniverse.registration.presentation.usecase

import androidx.annotation.StringRes
import org.codingforanimals.registration.presentation.R

sealed class UserAuthStatus {
    object Success : UserAuthStatus()
    object Loading : UserAuthStatus()
    sealed class Exception(@StringRes val title: Int, @StringRes val message: Int) :
        UserAuthStatus() {
        object UserAlreadyExists : Exception(
            title = R.string.register_error_user_already_exists_title,
            message = R.string.register_error_user_already_exists_message,
        )

        /**
         * Indicates either invalid or expired credentials
         */
        object InvalidCredentials : Exception(
            title = R.string.register_error_invalid_credentials_title,
            message = R.string.register_error_invalid_credentials_message,
        )

        /**
         * Indicates the user is invalid or was disabled.
         */
        object InvalidUser : Exception(
            title = R.string.register_error_invalid_user_title,
            message = R.string.register_error_invalid_user_message,
        )

        object UnknownFailure : Exception(
            title = R.string.register_error_unknown_failure_title,
            message = R.string.register_error_unknown_failure_message,
        )
    }
}