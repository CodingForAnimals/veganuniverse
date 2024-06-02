package org.codingforanimals.veganuniverse.profile.domain.repository

import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun downloadAndStoreProfile(): Boolean
    suspend fun getProfile(): Profile
    suspend fun editProfile(args: ProfileEditArguments): Boolean
}
