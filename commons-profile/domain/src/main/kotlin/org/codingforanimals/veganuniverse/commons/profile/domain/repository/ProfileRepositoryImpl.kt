package org.codingforanimals.veganuniverse.commons.profile.domain.repository

import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditActionValue
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.commons.profile.data.remote.ProfileRemoteDataSource
import org.codingforanimals.veganuniverse.commons.profile.data.storage.ProfileContent
import org.codingforanimals.veganuniverse.commons.profile.data.storage.ProfileLocalDataSource
import org.codingforanimals.veganuniverse.commons.profile.domain.model.Profile
import org.codingforanimals.veganuniverse.commons.profile.domain.model.toDomainModel
import org.codingforanimals.veganuniverse.commons.profile.domain.model.toProfileContent
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

internal class ProfileRepositoryImpl(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    override suspend fun downloadAndStoreProfile() {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to access their profile"
        }
        profileRemoteDataSource.getProfile(user.id)?.let {
            profileLocalDataSource.clearAllProfileContent()
            profileLocalDataSource.insertProfileContent(*it.toProfileContent().toTypedArray())
        }
    }

    override suspend fun clearProfile() {
        profileLocalDataSource.clearAllProfileContent()
    }

    override suspend fun getProfile(): Profile {
        return profileLocalDataSource.getAllProfileContent().toDomainModel()
    }

    override suspend fun editProfile(args: ProfileEditArguments) {
        profileRemoteDataSource.editProfile(args)
        when (args.profileEditActionValue) {
            ProfileEditActionValue.ADD -> {
                if (profileLocalDataSource.getProfileContent(args) == null) {
                    profileLocalDataSource.insertProfileContent(args.toProfileContent())
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
