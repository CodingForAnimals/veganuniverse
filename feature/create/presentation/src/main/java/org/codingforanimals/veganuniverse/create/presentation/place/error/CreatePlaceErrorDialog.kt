package org.codingforanimals.veganuniverse.create.presentation.place.error

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.create.presentation.R

open class CreatePlaceErrorDialog(@StringRes val title: Int, @StringRes val message: Int) {
    object UnknownErrorDialog : CreatePlaceErrorDialog(
        title = R.string.generic_error_title,
        message = R.string.unknown_error_message,
    )

    object PlaceTypeErrorDialog :
        CreatePlaceErrorDialog(
            title = R.string.place_type_error_dialog_title,
            message = R.string.place_type_error_dialog_message
        )

    object MissingCriticalFieldErrorDialog :
        CreatePlaceErrorDialog(
            title = R.string.place_missing_field_error_dialog_title,
            message = R.string.place_missing_field_error_dialog_message
        )

    object InvalidFormErrorDialog : CreatePlaceErrorDialog(
        title = R.string.invalid_form_title,
        message = R.string.invalid_form_message,
    )

    data class PlaceAlreadyExists(val navigateToExistingPlace: suspend () -> Unit) :
        CreatePlaceErrorDialog(
            title = R.string.place_already_exists_error_dialog_title,
            message = R.string.place_already_exists_error_dialog_message,
        )
}