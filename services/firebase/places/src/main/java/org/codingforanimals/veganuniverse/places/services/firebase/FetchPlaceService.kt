package org.codingforanimals.veganuniverse.places.services.firebase

import org.codingforanimals.veganuniverse.places.entity.Place as PlaceDomainEntity

interface FetchPlaceService {
    suspend fun byId(id: String): PlaceDomainEntity?
    suspend fun byIds(ids: List<String>): List<PlaceDomainEntity>
}

