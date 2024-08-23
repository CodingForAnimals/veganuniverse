package org.codingforanimals.veganuniverse.place.details

import android.util.Log
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place

private const val TAG = "GetPlaceDetails"

class GetPlaceDetails(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(geoHash: String): Result<Place> {
        return runCatching {
            placeRepository.getById(geoHash)!!
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Analytics.logNonFatalException(it)
        }
    }
}