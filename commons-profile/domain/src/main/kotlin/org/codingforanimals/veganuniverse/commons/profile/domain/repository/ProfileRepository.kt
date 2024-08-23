package org.codingforanimals.veganuniverse.commons.profile.domain.repository

import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.commons.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun downloadAndStoreProfile(): Boolean
    suspend fun getProfile(): Profile
    suspend fun editProfile(args: ProfileEditArguments): Boolean
}
