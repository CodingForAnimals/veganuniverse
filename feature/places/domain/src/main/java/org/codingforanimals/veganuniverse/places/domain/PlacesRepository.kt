package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.places.domain.model.PlaceDomainEntity
import org.codingforanimals.veganuniverse.places.domain.model.PlaceLocationQueryParams
import org.codingforanimals.veganuniverse.places.domain.model.ReviewDomainEntity
import org.codingforanimals.veganuniverse.places.domain.model.ReviewFormDomainEntity

interface PlacesRepository {
    /**
     * Temporary in-memory data until cache is implemented
     */
    val currentPlaces: List<PlaceDomainEntity>
    suspend fun getPlaces(params: PlaceLocationQueryParams): List<PlaceDomainEntity>
    fun getPlace(id: String): PlaceDomainEntity
    suspend fun getReview(placeId: String, userId: String): ReviewDomainEntity?
    suspend fun getReviews(id: String): List<ReviewDomainEntity>
    suspend fun submitReview(
        placeId: String,
        reviewForm: ReviewFormDomainEntity,
    ): ReviewDomainEntity
}