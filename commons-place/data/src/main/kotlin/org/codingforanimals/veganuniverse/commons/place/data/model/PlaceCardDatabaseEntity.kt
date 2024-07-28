package org.codingforanimals.veganuniverse.commons.place.data.model

import com.google.firebase.database.PropertyName

internal data class PlaceCardDatabaseEntity(
    @get:PropertyName(NAME)
    @set:PropertyName(NAME)
    var name: String? = null,

    @get:PropertyName(RATING)
    @set:PropertyName(RATING)
    var rating: Double? = null,

    @get:PropertyName(STREET_ADDRESS)
    @set:PropertyName(STREET_ADDRESS)
    var streetAddress: String? = null,

    @get:PropertyName(ADMINISTRATIVE_AREA)
    @set:PropertyName(ADMINISTRATIVE_AREA)
    var administrativeArea: String? = null,

    @get:PropertyName(TYPE)
    @set:PropertyName(TYPE)
    var type: String? = null,

    @get:PropertyName(TAGS)
    @set:PropertyName(TAGS)
    var tags: List<String>? = null,

    @get:PropertyName(IMAGE_ID)
    @set:PropertyName(IMAGE_ID)
    var imageId: String? = null,

    @get:PropertyName(VALIDATED)
    @set:PropertyName(VALIDATED)
    var validated: Boolean = false

) {
    private companion object {
        const val NAME = "n"
        const val RATING = "r"
        const val STREET_ADDRESS = "s_a"
        const val ADMINISTRATIVE_AREA = "a_a"
        const val TYPE = "ty"
        const val TAGS = "ta"
        const val IMAGE_ID = "i"
        const val VALIDATED = "v"
    }
}