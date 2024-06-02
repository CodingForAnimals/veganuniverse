package org.codingforanimals.veganuniverse.profile.data.remote

import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditArguments

interface ProfileRemoteDataSource {
    suspend fun getProfile(userId: String): ProfileDTO?
    suspend fun editProfile(args: ProfileEditArguments)
}
