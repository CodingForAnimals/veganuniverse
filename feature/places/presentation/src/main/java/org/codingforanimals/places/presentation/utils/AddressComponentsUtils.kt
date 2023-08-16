package org.codingforanimals.places.presentation.utils

import org.codingforanimals.veganuniverse.places.entity.AddressComponents

internal val AddressComponents.biggerAreaName: String
    get() {
        var biggerArea = locality
        biggerArea.ifEmpty { biggerArea = secondaryAdminArea }
        biggerArea.ifEmpty { biggerArea = primaryAdminArea }
        biggerArea.ifEmpty { biggerArea = country }
        return biggerArea
    }

internal val AddressComponents.fullStreetAddress: String
    get() = "$streetAddress - $biggerAreaName"