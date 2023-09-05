package org.codingforanimals.veganuniverse.services.google.places.api

import android.content.Intent
import com.google.android.gms.maps.model.LatLngBounds
import org.codingforanimals.veganuniverse.services.google.places.model.PlaceAutocompleteResult

interface PlacesClient {
    suspend fun getPlaceAutocompleteData(intent: Intent): PlaceAutocompleteResult
    fun getPlaceAutocompleteIntent(locationBias: LatLngBounds?): Intent
}