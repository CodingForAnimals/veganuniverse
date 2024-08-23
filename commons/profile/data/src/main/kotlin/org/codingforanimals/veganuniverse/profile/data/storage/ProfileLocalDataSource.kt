package org.codingforanimals.veganuniverse.profile.data.storage

import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditArguments

interface ProfileLocalDataSource {
    suspend fun getAllProfileContent(): List<ProfileContent>
    suspend fun getProfileContent(editArguments: ProfileEditArguments): ProfileContent?
    suspend fun insertProfileContent(vararg profileContent: ProfileContent): Boolean
    suspend fun deleteProfileContent(editArguments: ProfileEditArguments): Boolean
    suspend fun clearAllProfileContent()
}
