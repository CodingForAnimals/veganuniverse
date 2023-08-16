package org.codingforanimals.veganuniverse.places.services.firebase.api

import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceForm

interface PlacesApi {
    suspend fun fetchPlace(latitude: Double, longitude: Double): Place?
    suspend fun fetchPlaces(params: GeoLocationQueryParams): List<Place>
    suspend fun uploadPlace(form: PlaceForm)
}