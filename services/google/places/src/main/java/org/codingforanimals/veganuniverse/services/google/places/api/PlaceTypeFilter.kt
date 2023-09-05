package org.codingforanimals.veganuniverse.services.google.places.api

import com.google.android.libraries.places.api.model.PlaceTypes

enum class PlaceTypeFilter(val filter: String) {
    LOCATION(PlaceTypes.POLITICAL)
}