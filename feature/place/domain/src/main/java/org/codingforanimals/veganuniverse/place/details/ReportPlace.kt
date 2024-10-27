package org.codingforanimals.veganuniverse.place.details

import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.place.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class ReportPlace(
    private val placeRepository: PlaceRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(placeId: String): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to report a place"
        }
        placeRepository.reportPlace(placeId, user.id)
    }.onFailure {
        Log.e("ReportPlace", "Error reporting place", it)
        Analytics.logNonFatalException(it)
    }
}

