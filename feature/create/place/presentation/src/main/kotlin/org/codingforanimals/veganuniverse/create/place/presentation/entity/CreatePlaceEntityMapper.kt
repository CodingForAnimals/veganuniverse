package org.codingforanimals.veganuniverse.create.place.presentation.entity

import org.codingforanimals.veganuniverse.create.place.presentation.model.AddressField
import org.codingforanimals.veganuniverse.create.place.presentation.model.OpeningHours
import org.codingforanimals.veganuniverse.places.entity.AddressComponents
import org.codingforanimals.veganuniverse.ui.calendar.DayOfWeek
import org.codingforanimals.veganuniverse.places.entity.OpeningHours as DomainOpeningHours

internal fun AddressField.toAddressComponents(): AddressComponents {
    return AddressComponents(
        streetAddress = streetAddress.ifBlank { throw IllegalArgumentException() },
        locality = locality,
        primaryAdminArea = primaryAdminArea,
        secondaryAdminArea = secondaryAdminArea,
        country = country
    )
}

internal fun List<OpeningHours>.toDomainEntity(): List<org.codingforanimals.veganuniverse.places.entity.OpeningHours> {
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

internal fun DomainOpeningHours.toViewEntity(): OpeningHours {
    return OpeningHours(
        dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
        isClosed = mainPeriod == null,
        isSplit = secondaryPeriod != null,
        mainPeriod = mainPeriod ?: OpeningHours.defaultPeriod(),
        secondaryPeriod = secondaryPeriod ?: OpeningHours.defaultPeriod(),
    )
}