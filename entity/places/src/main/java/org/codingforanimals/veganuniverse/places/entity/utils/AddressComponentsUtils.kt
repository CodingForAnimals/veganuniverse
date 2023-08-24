package org.codingforanimals.veganuniverse.places.entity.utils

import org.codingforanimals.veganuniverse.places.entity.AddressComponents

val AddressComponents.fullStreetAddress: String
    get() = "$streetAddress - $administrativeArea"

val AddressComponents.administrativeArea: String
    get() = locality.ifEmpty { secondaryAdminArea.ifEmpty { primaryAdminArea.ifEmpty { country } } }