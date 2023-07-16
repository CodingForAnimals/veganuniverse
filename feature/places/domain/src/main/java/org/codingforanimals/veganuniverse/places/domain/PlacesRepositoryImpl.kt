package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.places.domain.model.PlaceDomainEntity
import org.codingforanimals.veganuniverse.places.domain.model.PlaceLocationQueryParams
import org.codingforanimals.veganuniverse.places.domain.model.ReviewDomainEntity
import org.codingforanimals.veganuniverse.places.domain.model.ReviewFormDomainEntity
import org.codingforanimals.veganuniverse.places.domain.model.ReviewsPaginatedResponseDTO
import org.codingforanimals.veganuniverse.places.domain.model.toDomainEntity
import org.codingforanimals.veganuniverse.places.domain.model.toDto
import org.codingforanimals.veganuniverse.places.domain.model.toFirebaseQueryParams
import org.codingforanimals.veganuniverse.services.firebase.places.api.PlacesApi

class PlacesRepositoryImpl(
    private val placesApi: PlacesApi,
    private val currentPlacesWrapper: CurrentPlacesWrapper,
) : PlacesRepository {

    override suspend fun getPlaces(params: PlaceLocationQueryParams): List<PlaceDomainEntity> {
        val places =
            placesApi.fetchPlaces(params.toFirebaseQueryParams()).mapNotNull { it.toDomainEntity() }
        currentPlacesWrapper.currentPlaces = places
        return places
    }

    override fun getPlace(id: String): PlaceDomainEntity {
        return currentPlacesWrapper.currentPlaces.first { it.id == id }
    }

    override suspend fun getReview(placeId: String, userId: String): ReviewDomainEntity? {
        return placesApi.fetchReview(placeId, userId)?.toDomainEntity()
    }

    override suspend fun getReviews(placeId: String): ReviewsPaginatedResponseDTO {
        val response = placesApi.fetchReviews(placeId)
        return ReviewsPaginatedResponseDTO(
            reviews = response.reviews.mapNotNull { it.toDomainEntity() },
            hasMoreItems = response.hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        reviewForm: ReviewFormDomainEntity,
    ): ReviewDomainEntity {
        return placesApi.submitReview(placeId, reviewForm.toDto()).toDomainEntity()
    }

    override suspend fun deleteReview(placeId: String, userId: String) {
        placesApi.deleteReview(placeId, userId)
    }
}

class CurrentPlacesWrapper {
    var currentPlaces: List<PlaceDomainEntity> = emptyList()
}