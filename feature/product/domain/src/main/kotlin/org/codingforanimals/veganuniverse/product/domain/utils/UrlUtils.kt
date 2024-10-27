package org.codingforanimals.veganuniverse.product.domain.utils

/**
 * Image path example: https://storage.googleapis.com/veganuniverse-a924e.appspot.com/content/products/picture/WhSVjjVzquyJ14m68HU9_800x800
 */
internal fun String.getImageIdFromFullPath(): String? {
    return split("/").lastOrNull()
}
