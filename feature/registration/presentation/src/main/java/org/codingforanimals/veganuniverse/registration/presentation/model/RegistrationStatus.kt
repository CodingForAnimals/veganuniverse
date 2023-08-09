package org.codingforanimals.veganuniverse.registration.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.registration.presentation.R

/**
 * Used for registration and provider auth
 */
sealed class RegistrationStatus {
    object Success : RegistrationStatus()
    object Loading : RegistrationStatus()
    sealed class Exception(@StringRes val title: Int, @StringRes val message: Int) :
        RegistrationStatus() {

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
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )

        object ConnectionError : Exception(
            title = R.string.sign_in_connection_error_title,
            message = R.string.sign_in_connection_error_message,
        )
    }
}