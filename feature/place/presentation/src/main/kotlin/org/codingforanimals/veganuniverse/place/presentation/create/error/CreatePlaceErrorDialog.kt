package org.codingforanimals.veganuniverse.place.presentation.create.error

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_title
import org.codingforanimals.veganuniverse.place.presentation.R

open class CreatePlaceErrorDialog(@StringRes val title: Int, @StringRes val message: Int) {
    object UnknownErrorDialog : CreatePlaceErrorDialog(
        title = unexpected_error_title,
        message = unexpected_error_message,
    )

    object PlaceTypeErrorDialog :
        CreatePlaceErrorDialog(
            title = R.string.place_type_error_dialog_title,
            message = R.string.place_type_error_dialog_message
        )

    data object PlaceAlreadyExists :
        CreatePlaceErrorDialog(
            title = R.string.place_already_exists_error_dialog_title,
            message = R.string.place_already_exists_error_dialog_message,
        )
}