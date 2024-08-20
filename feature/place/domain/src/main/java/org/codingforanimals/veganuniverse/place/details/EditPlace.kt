package org.codingforanimals.veganuniverse.place.details

import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class EditPlace(
    private val placeRepository: PlaceRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(placeId: String, edition: String): Result<Unit> {
        return runCatching {
            val user = checkNotNull(flowOnCurrentUser().first()) {
                "User must be logged in to edit a place"
            }
            placeRepository.editPlace(placeId, user.id, edition)
        }
    }
}
