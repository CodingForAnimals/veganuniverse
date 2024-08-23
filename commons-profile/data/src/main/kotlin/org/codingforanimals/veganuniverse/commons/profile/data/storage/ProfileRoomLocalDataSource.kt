package org.codingforanimals.veganuniverse.commons.profile.data.storage

import android.util.Log
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments

private const val TAG = "ProfileRoomLocalDataSou"

internal class ProfileRoomLocalDataSource(
    private val profileDao: ProfileDao,
) : ProfileLocalDataSource {
    override suspend fun getAllProfileContent(): List<ProfileContent> {
        return runCatching { profileDao.getAllProfileContent() }.onFailure { Log.e(TAG, it.stackTraceToString()) }.getOrElse { emptyList() }
    }

    override suspend fun getProfileContent(editArguments: ProfileEditArguments): ProfileContent? {
        return runCatching {
            profileDao.getProfileContent(
                contentId = editArguments.contentId,
                contentType = editArguments.profileEditContentType,
                actionType = editArguments.profileEditActionType,
            ).firstOrNull()
        }.getOrNull()
    }

    override suspend fun insertProfileContent(vararg profileContent: ProfileContent): Boolean {
        return runCatching {
            profileDao.insertProfileContent(*profileContent)
            true
        }.onFailure { Log.e(TAG, it.stackTraceToString()) }.getOrElse { false }
    }

    override suspend fun deleteProfileContent(editArguments: ProfileEditArguments): Boolean {
        return runCatching {
            profileDao.deleteProfileContent(
                contentId = editArguments.contentId,
                contentType = editArguments.profileEditContentType,
                actionType = editArguments.profileEditActionType,
            ) >= 1
        }.onFailure { Log.e(TAG, it.stackTraceToString()) }.getOrElse { false }
    }

    override suspend fun clearAllProfileContent() {
        profileDao.clearAllProfileContent()
    }
}
