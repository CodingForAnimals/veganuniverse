package org.codingforanimals.veganuniverse.core.ui.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.core.ui.R

data class Dialog(
    @StringRes val title: Int,
    @StringRes val message: Int,
) {
    @StringRes
    val back: Int = R.string.back

    companion object {
        fun unknownErrorDialog() = Dialog(
            title = R.string.generic_error_title,
            message = R.string.unknown_error_message,
        )
    }
}
