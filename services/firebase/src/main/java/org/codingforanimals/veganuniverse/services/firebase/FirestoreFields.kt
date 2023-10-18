package org.codingforanimals.veganuniverse.services.firebase

object FirestoreFields {
    const val ID = "id"
    const val USER_ID = "userId"
    const val CREATED_AT = "createdAt"
    const val TAGS = "tags"

    object Recipes {
        const val TITLE = "title"
        const val LIKES = "likes"
    }

    object Places {
        const val GEO_HASH = "geoHash"
        const val VERIFIED = "verified"
        const val TIMESTAMP = "timestamp"
    }
}