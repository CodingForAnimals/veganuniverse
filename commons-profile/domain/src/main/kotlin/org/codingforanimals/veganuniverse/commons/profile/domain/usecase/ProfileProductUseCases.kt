package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditActionType
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditActionValue
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditContentType
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.commons.profile.shared.model.ToggleResult
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private const val TAG = "ProfileProductUseCases"

internal class ProfileProductUseCases(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val profileRepository: ProfileRepository,
) : ProfileContentUseCases {

    override suspend fun isLiked(contentId: String): Boolean = false
    override suspend fun toggleLike(contentId: String, currentValue: Boolean): ToggleResult {
        TODO("Not yet implemented")
    }

    override suspend fun isBookmarked(contentId: String): Boolean {
        val profile = profileRepository.getProfile()
        return profile.bookmarks.products.contains(contentId)
    }

    override suspend fun toggleBookmark(contentId: String, currentValue: Boolean): ToggleResult {
        TODO("Not yet implemented")
    }

    override suspend fun isContributed(contentId: String): Boolean {
        val profile = profileRepository.getProfile()
        return profile.contributions.products.contains(contentId)
    }

    override suspend fun addContribution(contentId: String) {
        val args = ProfileEditArguments(
            userId = flowOnCurrentUser().firstOrNull()?.id ?: return,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.RECIPE,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.ADD,
        )
        runCatching { profileRepository.editProfile(args) }
            .onFailure { Log.e(TAG, it.stackTraceToString()) }
    }

    override suspend fun removeContribution(contentId: String) {
        val args = ProfileEditArguments(
            userId = flowOnCurrentUser().firstOrNull()?.id ?: return,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.RECIPE,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.REMOVE,
        )
        runCatching { profileRepository.editProfile(args) }
            .onFailure { Log.e(TAG, it.stackTraceToString()) }
    }
}
