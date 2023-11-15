package org.codingforanimals.veganuniverse.create.place.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.create.place.presentation.R

sealed class InvalidFormException(
    @StringRes val errorMessage: Int,
) : Exception() {
    data object EmptyName : InvalidFormException(R.string.empty_name_message)
    data object InvalidAddress : InvalidFormException(R.string.invalid_address_message)
    data object InvalidLocation : InvalidFormException(R.string.invalid_location_message)
    data object EmptyType : InvalidFormException(R.string.empty_type_message)
}