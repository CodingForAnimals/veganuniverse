package org.codingforanimals.veganuniverse.profile.domain.repository

import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditActionValue
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.profile.data.remote.ProfileRemoteDataSource
import org.codingforanimals.veganuniverse.profile.data.storage.ProfileContent
import org.codingforanimals.veganuniverse.profile.data.storage.ProfileLocalDataSource
import org.codingforanimals.veganuniverse.profile.domain.model.Profile
import org.codingforanimals.veganuniverse.profile.domain.model.toDomainModel
import org.codingforanimals.veganuniverse.profile.domain.model.toProfileContent
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser

internal class ProfileRepositoryImpl(
    private val getCurrentUser: GetCurrentUser,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {
    override suspend fun downloadAndStoreProfile(): Boolean {
        return runCatching {
            val id = getCurrentUser()?.id ?: return false
            profileRemoteDataSource.getProfile(id)?.let {
                profileLocalDataSource.clearAllProfileContent()
                profileLocalDataSource.insertProfileContent(*it.toProfileContent().toTypedArray())
            }
            true
        }.getOrElse { false }
    }

    override suspend fun getProfile(): Profile {
        return profileLocalDataSource.getAllProfileContent().toDomainModel()
    }

    override suspend fun editProfile(args: ProfileEditArguments): Boolean {
        profileRemoteDataSource.editProfile(args)
        return when (args.profileEditActionValue) {
            ProfileEditActionValue.ADD -> {
                if (profileLocalDataSource.getProfileContent(args) == null) {
                    profileLocalDataSource.insertProfileContent(args.toProfileContent())
                } else {
                    false
                }
            }

            ProfileEditActionValue.REMOVE -> {
                profileLocalDataSource.deleteProfileContent(args)
            }
        }
    }

    private fun ProfileEditArguments.toProfileContent(): ProfileContent {
        return ProfileContent(
            contentId = contentId,
            contentType = profileEditContentType,
            actionType = profileEditActionType,
        )
    }
}
