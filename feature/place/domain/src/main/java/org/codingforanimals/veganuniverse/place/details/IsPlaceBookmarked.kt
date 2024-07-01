package org.codingforanimals.veganuniverse.place.details

import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases

class IsPlaceBookmarked(
    private val profilePlaceUseCases: ProfileContentUseCases,
) {
    suspend operator fun invoke(placeId: String): Boolean {
        return profilePlaceUseCases.isBookmarked(placeId)
    }
}