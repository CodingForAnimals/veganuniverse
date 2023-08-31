package org.codingforanimals.veganuniverse.places.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.places.presentation.R
import org.codingforanimals.veganuniverse.places.presentation.entity.Place

internal sealed class GetPlaceDetailsStatus {
    data object Loading : GetPlaceDetailsStatus()
    data class Success(val place: Place) :
        GetPlaceDetailsStatus()

    sealed class Exception(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : GetPlaceDetailsStatus() {
        data object UnknownException : Exception(
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )
    }
}