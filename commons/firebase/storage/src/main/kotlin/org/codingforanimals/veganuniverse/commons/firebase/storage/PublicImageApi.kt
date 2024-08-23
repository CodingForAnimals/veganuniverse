package org.codingforanimals.veganuniverse.commons.firebase.storage

data class PublicImageApi(
    val basePath: String,
) {
    fun withImageStorageRef(imageId: String): String {
        return basePath + imageId
    }
    companion object {
        internal const val PUBLIC_IMAGE_BASE_PATH = "https://storage.googleapis.com"
    }
}
