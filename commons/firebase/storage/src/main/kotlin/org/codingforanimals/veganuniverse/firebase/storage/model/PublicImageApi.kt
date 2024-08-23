package org.codingforanimals.veganuniverse.firebase.storage.model

data class PublicImageApi(
    private val storageBucket: String,
) {
    fun getFilePath(path: String): String {
        return "$PUBLIC_IMAGE_BASE_PATH/$storageBucket/$path"
    }

    fun getFilePath(path: String, imageId: String): String {
        return "$PUBLIC_IMAGE_BASE_PATH/$storageBucket/$path/$imageId"
    }

    fun getFilePath(path: String, imageId: String, resolution: ResizeResolution): String {
        return "$PUBLIC_IMAGE_BASE_PATH/$storageBucket/$path/$imageId/${resolution.suffix}"
    }

    companion object {
        internal const val PUBLIC_IMAGE_BASE_PATH = "https://storage.googleapis.com"
    }
}
