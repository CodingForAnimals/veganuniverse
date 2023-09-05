package org.codingforanimals.veganuniverse.services.google.places.api

import android.content.Intent
import org.codingforanimals.veganuniverse.services.google.places.model.PlaceAutocompleteResult

interface PlacesClient {
    suspend fun getPlaceAutocompleteData(intent: Intent): PlaceAutocompleteResult
    fun getPlaceAutocompleteIntent(params: AutocompleteIntentParams): Intent
}