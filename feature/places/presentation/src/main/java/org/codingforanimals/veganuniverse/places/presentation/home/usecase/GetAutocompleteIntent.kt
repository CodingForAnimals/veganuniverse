package org.codingforanimals.veganuniverse.places.presentation.home.usecase

import android.content.Intent
import org.codingforanimals.veganuniverse.services.google.places.api.AutocompleteIntentParams
import org.codingforanimals.veganuniverse.services.google.places.api.PlaceTypeFilter
import org.codingforanimals.veganuniverse.services.google.places.api.PlacesClient

class GetAutocompleteIntent(
    private val placesClient: PlacesClient,
) {
    operator fun invoke(): Intent {
        return placesClient.getPlaceAutocompleteIntent(
            AutocompleteIntentParams(
                placeTypeFilter = PlaceTypeFilter.LOCATION,
            )
        )
    }
}