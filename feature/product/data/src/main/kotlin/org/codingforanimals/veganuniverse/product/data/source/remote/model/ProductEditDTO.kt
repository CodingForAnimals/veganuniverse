package org.codingforanimals.veganuniverse.product.data.source.remote.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class ProductEditDTO(
    @get:Exclude
    @set:Exclude
    var objectKey: String? = null,

    @get:PropertyName(ORIGINAL_KEY)
    @set:PropertyName(ORIGINAL_KEY)
    var originalKey: String? = null,

    @get:PropertyName(EDIT_USER_ID)
    @set:PropertyName(EDIT_USER_ID)
    var editUserId: String? = null,

    @get:PropertyName(EDIT_USERNAME)
    @set:PropertyName(EDIT_USERNAME)
    var editUsername: String? = null,

    @get:PropertyName(USER_ID)
    @set:PropertyName(USER_ID)
    var userId: String? = null,

    @get:PropertyName(USERNAME)
    @set:PropertyName(USERNAME)
    var username: String? = null,

    @get:PropertyName(PRODUCT_NAME)
    @set:PropertyName(PRODUCT_NAME)
    var name: String? = null,

    @get:PropertyName(PRODUCT_BRAND)
    @set:PropertyName(PRODUCT_BRAND)
    var brand: String? = null,

    @get:PropertyName(PRODUCT_TYPE)
    @set:PropertyName(PRODUCT_TYPE)
    var type: String? = null,

    @get:PropertyName(PRODUCT_CATEGORY)
    @set:PropertyName(PRODUCT_CATEGORY)
    var category: String? = null,

    @get:PropertyName(PRODUCT_IMAGE)
    @set:PropertyName(PRODUCT_IMAGE)
    var imageId: String? = null,

    @get:PropertyName(PRODUCT_DESCRIPTION)
    @set:PropertyName(PRODUCT_DESCRIPTION)
    var description: String? = null,

    @get:PropertyName(TIMESTAMP)
    @set:PropertyName(TIMESTAMP)
    var timestamp: Long? = null,

    @get:PropertyName(LAST_UPDATED)
    @set:PropertyName(LAST_UPDATED)
    var lastUpdated: Long? = null,

    @get:PropertyName(PRODUCT_SOURCE_URL)
    @set:PropertyName(PRODUCT_SOURCE_URL)
    var sourceUrl: String? = null,
) {
    companion object {
        private const val ORIGINAL_KEY = "ok"
        private const val EDIT_USER_ID = "euid"
        private const val EDIT_USERNAME = "eun"
        private const val USER_ID = "uid"
        private const val USERNAME = "un"
        private const val PRODUCT_NAME = "n"
        private const val PRODUCT_BRAND = "b"
        private const val PRODUCT_TYPE = "t"
        private const val PRODUCT_CATEGORY = "c"
        private const val PRODUCT_DESCRIPTION = "d"
        private const val PRODUCT_SOURCE_URL = "su"
        internal const val PRODUCT_IMAGE = "i"
        internal const val TIMESTAMP = "ts"
        internal const val LAST_UPDATED = "lts"
    }
}
