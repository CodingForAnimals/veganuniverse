package org.codingforanimals.veganuniverse.validator.place.domain

import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.place.shared.query.PlaceQueryParams

internal class GetUnvalidatedPlacesPaginationFlowUseCase(
    private val placeRepository: PlaceRepository,
) {
    operator fun invoke() = placeRepository.queryPlacesPagingDataFlow(
        PlaceQueryParams.Builder().withValidated(false).build()
    )
}
