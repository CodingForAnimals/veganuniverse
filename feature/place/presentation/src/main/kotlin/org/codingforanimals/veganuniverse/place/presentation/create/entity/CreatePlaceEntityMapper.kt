package org.codingforanimals.veganuniverse.place.presentation.create.entity

import org.codingforanimals.veganuniverse.place.presentation.create.model.AddressField
import org.codingforanimals.veganuniverse.place.shared.model.AddressComponents

internal fun AddressField.toAddressComponents(): AddressComponents {
    return AddressComponents(
        streetAddress = streetAddress.ifBlank { throw IllegalArgumentException() },
        locality = locality,
        primaryAdminArea = primaryAdminArea,
        secondaryAdminArea = secondaryAdminArea,
        country = country
    )
}
