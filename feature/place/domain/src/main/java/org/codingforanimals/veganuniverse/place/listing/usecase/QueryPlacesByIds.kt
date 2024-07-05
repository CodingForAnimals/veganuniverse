package org.codingforanimals.veganuniverse.place.listing.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place

class QueryPlacesByIds(
    private val placeRepository: PlaceRepository,
) {
    operator fun invoke(ids: List<String>): Flow<PagingData<Place>> {
        return placeRepository.queryPlacesPagingDataByIds(ids)
    }
}