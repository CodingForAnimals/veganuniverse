package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditActionType
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditActionValue
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditContentType
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private const val TAG = "ProfileProductUseCases"

internal class ProfileProductUseCases(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val profileRepository: ProfileRepository,
) : ProfileContentUseCases {

    override suspend fun isLiked(contentId: String): Boolean = false
    override suspend fun toggleLike(contentId: String, currentValue: Boolean): Result<Boolean> =
        runCatching { false }

    override suspend fun isBookmarked(contentId: String): Boolean = runCatching {
        val profile = profileRepository.getProfile()
        return profile.bookmarks.products.contains(contentId)
    }.getOrElse {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
        false
    }

    override suspend fun toggleBookmark(contentId: String, currentValue: Boolean): Result<Boolean> =
        runCatching {
            val user = checkNotNull(flowOnCurrentUser().first()) {
                "User must be logged in to toggle a product bookmark"
            }
            val actionValue =
                if (currentValue) ProfileEditActionValue.REMOVE else ProfileEditActionValue.ADD
            val args = ProfileEditArguments(
                userId = user.id,
                contentId = contentId,
                profileEditContentType = ProfileEditContentType.PRODUCT,
                profileEditActionType = ProfileEditActionType.BOOKMARK,
                profileEditActionValue = actionValue,
            )
            profileRepository.editProfile(args)
            !currentValue
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Analytics.logNonFatalException(it)
        }

    override suspend fun isContributed(contentId: String): Boolean = runCatching {
        val profile = profileRepository.getProfile()
        return profile.contributions.products.contains(contentId)
    }.getOrElse {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
        false
    }

    override suspend fun addContribution(contentId: String): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to contribute a product"
        }
        val args = ProfileEditArguments(
            userId = user.id,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.PRODUCT,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.ADD,
        )
        profileRepository.editProfile(args)
    }.onFailure {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
    }

    override suspend fun removeContribution(contentId: String): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to remove a product contribution"
        }
        val args = ProfileEditArguments(
            userId = user.id,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.PRODUCT,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.REMOVE,
        )
        profileRepository.editProfile(args)
    }.onFailure {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
    }
}
