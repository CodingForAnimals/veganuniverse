package org.codingforanimals.veganuniverse.services.firebase

object FirestoreCollection {
    object Content {
        object Places {
            const val ITEMS = "content/places/items"
            fun reviews(placeId: String) = "content/places/items/$placeId/reviews"
        }
    }
}