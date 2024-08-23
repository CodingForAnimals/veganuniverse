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

private const val TAG = "ProfileRecipeUseCases"

internal class ProfileRecipeUseCases(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val profileRepository: ProfileRepository,
) : ProfileContentUseCases {

    override suspend fun isLiked(contentId: String): Boolean {
        val profile = profileRepository.getProfile()
        return profile.likes.recipes.contains(contentId)
    }

    override suspend fun toggleLike(contentId: String, currentValue: Boolean): Result<Boolean> =
        runCatching {
            val user = checkNotNull(flowOnCurrentUser().first()) {
                "User must be logged in to toggle a recipe like"
            }
            val actionValue =
                if (currentValue) ProfileEditActionValue.REMOVE else ProfileEditActionValue.ADD
            val args = ProfileEditArguments(
                userId = user.id,
                contentId = contentId,
                profileEditContentType = ProfileEditContentType.RECIPE,
                profileEditActionType = ProfileEditActionType.LIKE,
                profileEditActionValue = actionValue,
            )
            profileRepository.editProfile(args)
            !currentValue
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Analytics.logNonFatalException(it)
        }


    override suspend fun isBookmarked(contentId: String): Boolean = runCatching {
        val profile = profileRepository.getProfile()
        return profile.bookmarks.recipes.contains(contentId)
    }.getOrElse {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
        false
    }

    override suspend fun toggleBookmark(contentId: String, currentValue: Boolean): Result<Boolean> =
        runCatching {
            val user = checkNotNull(flowOnCurrentUser().first()) {
                "User must be logged in to toggle a recipe bookmark"
            }
            val actionValue =
                if (currentValue) ProfileEditActionValue.REMOVE else ProfileEditActionValue.ADD
            val args = ProfileEditArguments(
                userId = user.id,
                contentId = contentId,
                profileEditContentType = ProfileEditContentType.RECIPE,
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
        return profile.contributions.recipes.contains(contentId)
    }.getOrElse {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
        false
    }

    override suspend fun addContribution(contentId: String): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to contribute a recipe"
        }
        val args = ProfileEditArguments(
            userId = user.id,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.RECIPE,
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
            "User must be logged in to remove a recipe contribution"
        }
        val args = ProfileEditArguments(
            userId = user.id,
            contentId = contentId,
            profileEditContentType = ProfileEditContentType.RECIPE,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.REMOVE,
        )
        profileRepository.editProfile(args)
    }.onFailure {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
    }
}
