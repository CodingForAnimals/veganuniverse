package org.codingforanimals.veganuniverse.services.firebase

object StoragePath {
    private const val FIREBASE_STORAGE_BASE_URL = "https://storage.googleapis.com"
    const val PLACES_PICTURE_PATH = "content/places/picture"

    fun getPlaceImageRef(storageBucket: String, geoHash: String, fileExtension: String): String {
        return "$FIREBASE_STORAGE_BASE_URL/$storageBucket/$PLACES_PICTURE_PATH/$geoHash$fileExtension"
    }
}