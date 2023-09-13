package org.codingforanimals.veganuniverse.services.firebase

object StoragePath {
    private const val FIREBASE_STORAGE_BASE_URL = "https://storage.googleapis.com"
    const val PLACES_PICTURE_PATH = "content/places/picture"
    const val USERS_PICTURE_PATH = "users/picture"

    fun getPlaceImageRef(storageBucket: String, geoHash: String, fileExtension: String): String {
        return "$FIREBASE_STORAGE_BASE_URL/$storageBucket/$PLACES_PICTURE_PATH/$geoHash$fileExtension"
    }

    fun getUserPictureRef(storageBucket: String, userId: String): String {
        return "$FIREBASE_STORAGE_BASE_URL/$storageBucket/$USERS_PICTURE_PATH/${userId}_1600x1600.jpeg"
    }
}