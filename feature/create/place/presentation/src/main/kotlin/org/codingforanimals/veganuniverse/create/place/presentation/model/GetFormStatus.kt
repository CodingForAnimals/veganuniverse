package org.codingforanimals.veganuniverse.create.place.presentation.model

import org.codingforanimals.veganuniverse.places.entity.PlaceForm

sealed class GetFormStatus {
    data object Error : GetFormStatus()

    data class Success(
        val form: PlaceForm,
    ) : GetFormStatus()
}
