package org.codingforanimals.veganuniverse.place.listing.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.place.repository.PlaceRepository
import org.codingforanimals.veganuniverse.place.shared.model.Place

class QueryPlacesByIds(
    private val placeRepository: PlaceRepository,
) {
    operator fun invoke(ids: List<String>): Flow<PagingData<Place>> {
        return placeRepository.queryPlacesPagingDataByIds(ids)
    }
}