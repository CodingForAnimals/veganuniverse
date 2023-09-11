package org.codingforanimals.veganuniverse.services.firebase

object DatabasePath {
    object Content {
        object Places {
            fun card(placeId: String) = "content/places/cards/$placeId"
            const val GEO_FIRE = "content/places/geofire"
        }
    }
}