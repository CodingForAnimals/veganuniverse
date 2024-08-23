package org.codingforanimals.veganuniverse.place.details

import org.codingforanimals.veganuniverse.place.domain.repository.PlaceRepository

class ReportPlace(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(placeId: String) {

    }

    sealed class Result {
        data object UnauthenticatedUser : Result()
        data object UnexpectedError : Result()
        data object Success : Result()
    }
}