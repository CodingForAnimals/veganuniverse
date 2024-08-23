package org.codingforanimals.veganuniverse.commons.profile.data.storage

import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments

interface ProfileLocalDataSource {
    suspend fun getAllProfileContent(): List<ProfileContent>
    suspend fun getProfileContent(editArguments: ProfileEditArguments): ProfileContent?
    suspend fun insertProfileContent(vararg profileContent: ProfileContent)
    suspend fun deleteProfileContent(editArguments: ProfileEditArguments)
    suspend fun clearAllProfileContent()
}
