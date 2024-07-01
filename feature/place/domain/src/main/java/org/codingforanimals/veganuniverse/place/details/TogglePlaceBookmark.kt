package org.codingforanimals.veganuniverse.place.details

import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.profile.shared.model.ToggleResult

class TogglePlaceBookmark(
    private val profilePlaceUseCases: ProfileContentUseCases,
) {
    suspend operator fun invoke(placeId: String, currentValue: Boolean): ToggleResult {
        return profilePlaceUseCases.toggleBookmark(placeId, currentValue)
    }
}
