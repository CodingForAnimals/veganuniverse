package org.codingforanimals.veganuniverse.profile.domain

import android.net.Uri
import org.codingforanimals.veganuniverse.profile.domain.model.UserFeatureContributions
import org.codingforanimals.veganuniverse.services.firebase.api.PlacesApi
import org.codingforanimals.veganuniverse.services.firebase.model.FetchPlacesQueryParams
import org.codingforanimals.veganuniverse.user.services.firebase.AccountUpdatesManager

internal class ProfileRepositoryImpl(
    private val placesApi: PlacesApi,
    private val accountUpdatesManager: AccountUpdatesManager,
) : ProfileRepository {

    override suspend fun getUserFeatureContributions(userId: String): UserFeatureContributions {
        val params = FetchPlacesQueryParams.Builder().userId(userId).build()
        val places = placesApi.fetchPlaces(params)
        return UserFeatureContributions(
            places = places
        )
    }

    override suspend fun uploadNewProfilePicture(uri: Uri) {
        accountUpdatesManager.updateProfilePicture(uri)
    }
}

