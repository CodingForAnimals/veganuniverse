package org.codingforanimals.veganuniverse.commons.ui.dialog

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.ui.R

data class Dialog(
    @StringRes val title: Int,
    @StringRes val message: Int,
) {
    @StringRes
    val back: Int = R.string.back

    companion object {
        fun unknownErrorDialog() = Dialog(
            title = R.string.unexpected_error_title,
            message = R.string.unexpected_error_message,
        )
    }
}
