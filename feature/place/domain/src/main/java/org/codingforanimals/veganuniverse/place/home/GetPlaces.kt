package org.codingforanimals.veganuniverse.place.home

import android.util.Log
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceCard

private const val TAG = "GetPlaces"

class GetPlaces(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        radiusKm: Double,
    ): Result {
        return runCatching {
            val adjustedRadiusKm = if (radiusKm >= 5.0) 5.0 else radiusKm
            val params = GeoLocationQueryParams(
                latitude = latitude,
                longitude = longitude,
                radiusKm = adjustedRadiusKm,
            )
            val places = placeRepository.queryCardsByGeoLocation(params)
            Result.Success(places)
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.Error
        }
    }

    sealed class Result {
        data object Error : Result()
        data class Success(val places: List<PlaceCard>) : Result()
    }
}