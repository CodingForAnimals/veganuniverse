package org.codingforanimals.veganuniverse.services.google.places.api

import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.Place

data class AutocompleteIntentParams(
    val locationBiasBounds: LatLngBounds? = null,
    val placeTypeFilter: PlaceTypeFilter? = null,
    val placeFields: List<Place.Field> = fields,
    val countries: List<String> = listOf(ARGENTINA),
) {
    companion object {
        private val fields = listOf(
            Place.Field.NAME,
            Place.Field.TYPES,
            Place.Field.LAT_LNG,
            Place.Field.OPENING_HOURS,
            Place.Field.PHOTO_METADATAS,
            Place.Field.ADDRESS_COMPONENTS,
        )
        private const val ARGENTINA = "AR"
    }
}