package org.codingforanimals.veganuniverse.additives.data.source.remote.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class AdditiveEditDTO(
    @get:Exclude
    @set:Exclude
    var objectKey: String? = null,

    @get:PropertyName(ADDITIVE_ID)
    @set:PropertyName(ADDITIVE_ID)
    var additiveID: String? = null,

    @get:PropertyName(USER_ID)
    @set:PropertyName(USER_ID)
    var userID: String? = null,

    @get:PropertyName(CODE)
    @set:PropertyName(CODE)
    var code: String? = null,

    @get:PropertyName(NAME)
    @set:PropertyName(NAME)
    var name: String? = null,

    @get:PropertyName(DESCRIPTION)
    @set:PropertyName(DESCRIPTION)
    var description: String? = null,

    @get:PropertyName(TYPE)
    @set:PropertyName(TYPE)
    var type: String? = null,
) {
    companion object {
        private const val ADDITIVE_ID = "aid"
        private const val USER_ID = "uid"
        private const val CODE = "c"
        private const val NAME = "n"
        private const val DESCRIPTION = "d"
        private const val TYPE = "t"
    }
}
