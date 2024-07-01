package org.codingforanimals.veganuniverse.create.place.presentation.entity

import org.codingforanimals.veganuniverse.create.place.presentation.model.AddressField
import org.codingforanimals.veganuniverse.commons.place.shared.model.AddressComponents

internal fun AddressField.toAddressComponents(): AddressComponents {
    return AddressComponents(
        streetAddress = streetAddress.ifBlank { throw IllegalArgumentException() },
        locality = locality,
        primaryAdminArea = primaryAdminArea,
        secondaryAdminArea = secondaryAdminArea,
        country = country
    )
}
