package org.codingforanimals.veganuniverse.place.details

import org.codingforanimals.veganuniverse.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.profile.model.ToggleResult

class TogglePlaceBookmark(
    private val profilePlaceUseCases: ProfileContentUseCases,
) {
    suspend operator fun invoke(placeId: String, currentValue: Boolean): ToggleResult {
        return profilePlaceUseCases.toggleBookmark(placeId, currentValue)
    }
}

