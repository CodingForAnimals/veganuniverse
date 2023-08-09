package org.codingforanimals.veganuniverse.create.presentation.place.model

import org.codingforanimals.veganuniverse.create.domain.model.PlaceFormDomainEntity

sealed class GetFormStatus {
    object Error : GetFormStatus()

    data class Success(
        val form: PlaceFormDomainEntity,
    ) : GetFormStatus()
}
