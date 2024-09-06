package org.codingforanimals.veganuniverse.validator.place.domain

import android.util.Log
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository

internal class ValidatePlaceUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return runCatching {
            placeRepository.validatePlace(id)
        }.onFailure {
            Log.e(TAG, "Error validating place", it)
        }
    }

    companion object {
        private const val TAG = "ValidatePlaceUseCase"
    }
}
