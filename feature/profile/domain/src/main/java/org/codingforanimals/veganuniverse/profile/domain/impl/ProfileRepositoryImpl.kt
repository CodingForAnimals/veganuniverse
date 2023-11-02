package org.codingforanimals.veganuniverse.profile.domain.impl

import android.net.Uri
import org.codingforanimals.veganuniverse.places.services.firebase.PlacesService
import org.codingforanimals.veganuniverse.profile.domain.ProfileRepository
import org.codingforanimals.veganuniverse.user.services.firebase.AccountUpdatesManager

internal class ProfileRepositoryImpl(
    private val placesService: PlacesService,
    private val accountUpdatesManager: AccountUpdatesManager,
) : ProfileRepository {

    override suspend fun uploadNewProfilePicture(uri: Uri) {
        accountUpdatesManager.updateProfilePicture(uri)
    }
}
