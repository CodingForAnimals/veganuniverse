package org.codingforanimals.veganuniverse.profile.domain

import android.net.Uri
import org.codingforanimals.veganuniverse.profile.domain.model.UserFeatureContributions

interface ProfileRepository {
    suspend fun getUserFeatureContributions(userId: String): UserFeatureContributions
    suspend fun uploadNewProfilePicture(uri: Uri)
}