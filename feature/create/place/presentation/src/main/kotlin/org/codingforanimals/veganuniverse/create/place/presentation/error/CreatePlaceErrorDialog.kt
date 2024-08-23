package org.codingforanimals.veganuniverse.create.place.presentation.error

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.create.place.presentation.R
import org.codingforanimals.veganuniverse.ui.R.string.generic_error_title
import org.codingforanimals.veganuniverse.ui.R.string.unknown_error_message

open class CreatePlaceErrorDialog(@StringRes val title: Int, @StringRes val message: Int) {
    object UnknownErrorDialog : CreatePlaceErrorDialog(
        title = generic_error_title,
        message = unknown_error_message,
    )

    object PlaceTypeErrorDialog :
        CreatePlaceErrorDialog(
            title = R.string.place_type_error_dialog_title,
            message = R.string.place_type_error_dialog_message
        )

    object InvalidFormErrorDialog : CreatePlaceErrorDialog(
        title = R.string.invalid_form_title,
        message = R.string.invalid_form_message,
    )

    object NoInternet : CreatePlaceErrorDialog(
        title = R.string.no_internet_title,
        message = R.string.no_internet_message,
    )

    object UnverifiedEmail : CreatePlaceErrorDialog(
        title = R.string.generic_error_title,
        message = R.string.email_not_yet_verified,
    )

    data object PlaceAlreadyExists :
        CreatePlaceErrorDialog(
            title = R.string.place_already_exists_error_dialog_title,
            message = R.string.place_already_exists_error_dialog_message,
        )
}