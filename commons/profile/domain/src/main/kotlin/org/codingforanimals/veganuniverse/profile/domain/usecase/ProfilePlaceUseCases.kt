package org.codingforanimals.veganuniverse.profile.domain.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditActionType
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditActionValue
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditContentType
import org.codingforanimals.veganuniverse.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.profile.model.ToggleResult
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser

internal class ProfilePlaceUseCases(
    private val getCurrentUser: GetCurrentUser,
    private val profileRepository: ProfileRepository,
) : ProfileContentUseCases {
    override suspend fun isLiked(contentId: String): Boolean = false
    override suspend fun toggleLike(contentId: String, currentValue: Boolean): ToggleResult =
        ToggleResult.UnexpectedError(false)

    override suspend fun isBookmarked(contentId: String): Boolean {
        val profile = profileRepository.getProfile()
        return profile.bookmarks.places.contains(contentId)
    }

    override suspend fun toggleBookmark(contentId: String, currentValue: Boolean): ToggleResult {
        val actionValue =
            if (currentValue) ProfileEditActionValue.REMOVE else ProfileEditActionValue.ADD
        val args = ProfileEditArguments(
            userId = getCurrentUser()?.id ?: return ToggleResult.GuestUser(currentValue),
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.PLACE,
            profileEditActionType = ProfileEditActionType.BOOKMARK,
            profileEditActionValue = actionValue,
        )
        return attemptEdit(args, currentValue)
    }

    override suspend fun isContributed(contentId: String): Boolean {
        val profile = profileRepository.getProfile()
        return profile.contributions.places.contains(contentId)
    }

    override suspend fun addContribution(contentId: String) {
        val args = ProfileEditArguments(
            userId = getCurrentUser()?.id ?: return,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.PLACE,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.ADD,
        )
        runCatching { profileRepository.editProfile(args) }
            .onFailure { Log.e(TAG, it.stackTraceToString()) }
    }

    override suspend fun removeContribution(contentId: String) {
        val args = ProfileEditArguments(
            userId = getCurrentUser()?.id ?: return,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.PLACE,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.REMOVE,
        )
        runCatching { profileRepository.editProfile(args) }
            .onFailure { Log.e(TAG, it.stackTraceToString()) }
    }

    private suspend fun attemptEdit(
        args: ProfileEditArguments,
        currentValue: Boolean
    ): ToggleResult {
        return runCatching {
            profileRepository.editProfile(args)
            ToggleResult.Success(newValue = !currentValue)
        }.onFailure { Log.e(TAG, it.stackTraceToString()) }
            .getOrNull() ?: ToggleResult.UnexpectedError(currentValue)
    }
}

private const val TAG = "ProfilePlaceUseCases"