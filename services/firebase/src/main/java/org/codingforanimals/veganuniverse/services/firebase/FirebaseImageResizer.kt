package org.codingforanimals.veganuniverse.services.firebase

object FirebaseImageResizer {
    fun getPlacePictureToResizePath(geoHash: String): String {
        return "${StoragePath.PLACES_PICTURE_PATH}/$geoHash"
    }

    fun getRecipePictureToResizePath(recipeId: String): String {
        return "${StoragePath.RECIPES_PICTURE_PATH}/$recipeId"
    }
}