package org.codingforanimals.veganuniverse.services.firebase

/**
 * These methods are tied to the resolution specified in the image resizer extension in firebase
 */
object FirebaseImageResizer {
    fun getPlacePictureToResizeStorageReference(geoHash: String): String {
        return "${StoragePath.PLACES_PICTURE_PATH}/$geoHash.jpeg"
    }
}