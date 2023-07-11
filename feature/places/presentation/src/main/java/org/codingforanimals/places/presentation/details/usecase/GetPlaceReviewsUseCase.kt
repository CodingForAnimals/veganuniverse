package org.codingforanimals.places.presentation.details.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.places.presentation.model.GetPlaceReviewsStatus
import org.codingforanimals.places.presentation.model.toViewEntity
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository

class GetPlaceReviewsUseCase(
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(placeId: String): Flow<GetPlaceReviewsStatus> = flow {
        emit(GetPlaceReviewsStatus.Loading)
        val status = try {
            val reviews = placesRepository.getReviews(placeId).mapNotNull { it.toViewEntity() }
            GetPlaceReviewsStatus.Success(reviews = reviews)
        } catch (e: Throwable) {
            GetPlaceReviewsStatus.Exception
        }
        emit(status)
    }
}