package org.codingforanimals.veganuniverse.additives.data.source.remote.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class AdditiveDTO(
    @get:Exclude
    @set:Exclude
    var objectKey: String? = null,

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
        private const val CODE = "c"
        private const val NAME = "n"
        private const val DESCRIPTION = "d"
        private const val TYPE = "t"
    }
}