package org.codingforanimals.veganuniverse.places.services.firebase

import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceCard
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.services.firebase.model.FetchPlacesQueryParams

interface PlacesService {
    suspend fun fetchPlacesCards(params: GeoLocationQueryParams): List<PlaceCard>
    suspend fun uploadPlace(form: PlaceForm): String
    suspend fun fetchPlace(latitude: Double, longitude: Double): Place?
    suspend fun fetchPlaces(params: FetchPlacesQueryParams): List<Place>
    suspend fun fetchPlaces(ids: List<String>): List<Place>
    suspend fun fetchPlace(geoHash: String): Place?
}