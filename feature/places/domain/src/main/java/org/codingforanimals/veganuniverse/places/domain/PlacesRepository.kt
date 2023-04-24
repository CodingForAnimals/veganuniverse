package org.codingforanimals.veganuniverse.places.domain

interface PlacesRepository {
    suspend fun getPlaces(bounds: List<PlaceQueryBound>): List<PlaceDomainEntity>
}