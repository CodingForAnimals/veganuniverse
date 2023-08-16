package org.codingforanimals.veganuniverse.create.presentation.place.entity

import org.codingforanimals.veganuniverse.create.presentation.model.AddressField
import org.codingforanimals.veganuniverse.create.presentation.place.model.OpeningHours
import org.codingforanimals.veganuniverse.places.entity.AddressComponents

internal fun AddressField.toAddressComponents(): AddressComponents {
    return AddressComponents(
        streetAddress = streetAddress,
        locality = locality,
        primaryAdminArea = primaryAdminArea,
        secondaryAdminArea = secondaryAdminArea,
        country = country
    )
}

internal fun List<OpeningHours>.toAddressComponents(): List<org.codingforanimals.veganuniverse.places.entity.OpeningHours> {
    return map {
        if (it.isClosed) {
            org.codingforanimals.veganuniverse.places.entity.OpeningHours(
                dayOfWeek = it.dayOfWeek.name,
                mainPeriod = null,
                secondaryPeriod = null,
            )
        } else {
            val secondaryPeriod = if (it.isSplit) {
                it.secondaryPeriod
            } else {
                null
            }
            org.codingforanimals.veganuniverse.places.entity.OpeningHours(
                dayOfWeek = it.dayOfWeek.name,
                mainPeriod = it.mainPeriod,
                secondaryPeriod = secondaryPeriod,
            )
        }
    }
}