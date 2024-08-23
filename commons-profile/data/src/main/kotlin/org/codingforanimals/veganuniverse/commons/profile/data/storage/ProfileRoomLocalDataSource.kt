package org.codingforanimals.veganuniverse.commons.profile.data.storage

import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments

internal class ProfileRoomLocalDataSource(
    private val profileDao: ProfileDao,
) : ProfileLocalDataSource {
    override suspend fun getAllProfileContent(): List<ProfileContent> {
        return profileDao.getAllProfileContent()
    }

    override suspend fun getProfileContent(editArguments: ProfileEditArguments): ProfileContent? {
        return profileDao.getProfileContent(
            contentId = editArguments.contentId,
            contentType = editArguments.profileEditContentType,
            actionType = editArguments.profileEditActionType,
        ).firstOrNull()
    }

    override suspend fun insertProfileContent(vararg profileContent: ProfileContent) {
        return profileDao.insertProfileContent(*profileContent)
    }

    override suspend fun deleteProfileContent(editArguments: ProfileEditArguments) {
        profileDao.deleteProfileContent(
            contentId = editArguments.contentId,
            contentType = editArguments.profileEditContentType,
            actionType = editArguments.profileEditActionType,
        ) >= 1
    }

    override suspend fun clearAllProfileContent() {
        profileDao.clearAllProfileContent()
    }
}
