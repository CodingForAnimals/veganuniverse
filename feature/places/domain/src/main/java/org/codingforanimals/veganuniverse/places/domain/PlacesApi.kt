package org.codingforanimals.veganuniverse.places.domain

interface PlacesApi {
    suspend fun fetchPlaces(bounds: List<PlaceQueryBound>): List<PlaceDomainEntity>
}