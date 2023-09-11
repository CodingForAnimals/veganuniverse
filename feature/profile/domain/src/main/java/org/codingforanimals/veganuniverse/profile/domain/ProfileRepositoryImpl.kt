package org.codingforanimals.veganuniverse.profile.domain

import org.codingforanimals.veganuniverse.profile.domain.model.UserFeatureContributions
import org.codingforanimals.veganuniverse.services.firebase.api.PlacesApi
import org.codingforanimals.veganuniverse.services.firebase.model.FetchPlacesQueryParams

internal class ProfileRepositoryImpl(
    private val placesApi: PlacesApi,
) : ProfileRepository {

    override suspend fun getUserFeatureContributions(userId: String): UserFeatureContributions {
        val params = FetchPlacesQueryParams.Builder().userId(userId).build()
        val places = placesApi.fetchPlaces(params)
        return UserFeatureContributions(
            places = places
        )
    }
}

