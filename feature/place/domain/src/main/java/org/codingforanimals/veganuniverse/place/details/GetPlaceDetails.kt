package org.codingforanimals.veganuniverse.place.details

import android.util.Log
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place

private const val TAG = "GetPlaceDetails"

class GetPlaceDetails(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(geoHash: String): Result {
        return runCatching {
            Result.Success(placeRepository.getById(geoHash)!!)
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnexpectedError : Result()
        data class Success(val place: Place) : Result()
    }
}