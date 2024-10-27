package org.codingforanimals.veganuniverse.place.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.place.repository.PlaceRepository

class ValidatePlaceUseCase(
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
