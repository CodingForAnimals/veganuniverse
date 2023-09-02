package org.codingforanimals.veganuniverse.services.firebase.api

import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceCard
import org.codingforanimals.veganuniverse.places.entity.PlaceForm

interface PlacesApi {
    suspend fun fetchPlaces(params: GeoLocationQueryParams): List<PlaceCard>
    suspend fun uploadPlace(form: PlaceForm)
    suspend fun fetchPlace(latitude: Double, longitude: Double): Place?
    suspend fun fetchPlace(geoHash: String): Place?
}