package org.codingforanimals.veganuniverse.commons.profile.data.remote

import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments

interface ProfileRemoteDataSource {
    suspend fun getProfile(userId: String): ProfileDTO?
    suspend fun editProfile(args: ProfileEditArguments)
}
