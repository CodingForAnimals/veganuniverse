package org.codingforanimals.veganuniverse.place.reviews

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewQueryParams
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewSorter
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewUserFilter
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class GetLatestPlaceReviewsPagingFlow(
    private val placeReviewRepository: PlaceReviewRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    operator fun invoke(placeId: String): Flow<PagingData<PlaceReview>> = flow {
        var params = PlaceReviewQueryParams.Builder(placeId)
            .withPageSize(10)
            .withSorter(PlaceReviewSorter.DATE)

        val userId = flowOnCurrentUser().firstOrNull()?.id
        userId?.let { params = params.withUserFilter(PlaceReviewUserFilter.ExcludeUser(it)) }

        emitAll(placeReviewRepository.queryPlaceReviewsPagingFlow(params.build()))
    }
}


