package org.codingforanimals.veganuniverse.place.usecase

import org.codingforanimals.veganuniverse.place.repository.PlaceRepository
import org.codingforanimals.veganuniverse.place.shared.query.PlaceQueryParams

class GetUnvalidatedPlacesPaginationFlowUseCase(
    private val placeRepository: PlaceRepository,
) {
    operator fun invoke() = placeRepository.queryPlacesPagingDataFlow(
        PlaceQueryParams.Builder().withValidated(false).build()
    )
}
