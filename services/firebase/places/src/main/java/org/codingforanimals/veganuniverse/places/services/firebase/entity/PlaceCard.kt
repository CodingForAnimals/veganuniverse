package org.codingforanimals.veganuniverse.places.services.firebase.entity

import com.google.firebase.database.PropertyName

internal data class PlaceCard(
    @get:PropertyName(NAME)
    @set:PropertyName(NAME)
    var name: String = "",

    @get:PropertyName(RATING)
    @set:PropertyName(RATING)
    var rating: Double = 0.0,

    @get:PropertyName(STREET_ADDRESS)
    @set:PropertyName(STREET_ADDRESS)
    var streetAddress: String = "",

    @get:PropertyName(ADMINISTRATIVE_AREA)
    @set:PropertyName(ADMINISTRATIVE_AREA)
    var administrativeArea: String = "",

    @get:PropertyName(TYPE)
    @set:PropertyName(TYPE)
    var type: String = "",

    @get:PropertyName(TAGS)
    @set:PropertyName(TAGS)
    var tags: List<String> = emptyList(),

    @get:PropertyName(TIMESTAMP)
    @set:PropertyName(TIMESTAMP)
    var timestamp: Long = 0,
) {
    private companion object {
        const val NAME = "n"
        const val RATING = "r"
        const val STREET_ADDRESS = "s_a"
        const val ADMINISTRATIVE_AREA = "a_a"
        const val TYPE = "ty"
        const val TAGS = "ta"
        const val TIMESTAMP = "t"
    }
}