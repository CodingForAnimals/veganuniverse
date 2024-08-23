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

interface ProfilePlaceReviewUseCases {
    suspend fun addPlaceReview(placeId: String)
    suspend fun removePlaceReview(placeId: String)
}

internal class ProfilePlaceReviewUseCasesImpl(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val profileRepository: ProfileRepository,
) : ProfilePlaceReviewUseCases {
    override suspend fun addPlaceReview(placeId: String) {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to add a place review"
        }
        
        val args = ProfileEditArguments(
            userId = user.id,
            contentId = placeId,
            profileEditContentType = ProfileEditContentType.PLACE_REVIEW,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.ADD,
        )
        
        profileRepository.editProfile(args)
    }

    override suspend fun removePlaceReview(placeId: String) {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to remove a place review"
        }

        val args = ProfileEditArguments(
            userId = user.id,
            contentId = placeId,
            profileEditContentType = ProfileEditContentType.PLACE_REVIEW,
            profileEditActionType = ProfileEditActionType.CONTRIBUTION,
            profileEditActionValue = ProfileEditActionValue.REMOVE,
        )
        
        profileRepository.editProfile(args)
    }

    companion object {
        private const val TAG = "ProfilePlaceReviewUseCa"
    }
}
