package org.codingforanimals.veganuniverse.place.details

import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.place.repository.PlaceRepository
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
        }.onFailure {
            Log.e("EditPlace", "Error editing place", it)
            Analytics.logNonFatalException(it)
        }
    }
}
