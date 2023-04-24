package org.codingforanimals.veganuniverse.places.domain

class PlacesRepositoryImpl(
    private val placesApi: PlacesApi,
) : PlacesRepository {

    override suspend fun getPlaces(bounds: List<PlaceQueryBound>): List<PlaceDomainEntity> {
        return placesApi.fetchPlaces(bounds)
    }
}