package org.codingforanimals.places.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.places.presentation.entity.PlaceViewEntity
import org.codingforanimals.veganuniverse.places.presentation.R

internal sealed class GetPlaceDetailsStatus {
    object Loading : GetPlaceDetailsStatus()
    data class Success(val place: PlaceViewEntity) : GetPlaceDetailsStatus()

    sealed class Exception(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : GetPlaceDetailsStatus() {
        object UnknownException : Exception(
            title = R.string.error_unknown_failure_title,
            message = R.string.error_unknown_failure_message,
        )
    }
}