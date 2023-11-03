package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.entity.PaginatedResponse
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceCard
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm

interface PlacesRepository {
    suspend fun getPlace(id: String): Place?
    suspend fun getPlaces(params: GeoLocationQueryParams): List<PlaceCard>
    suspend fun getReview(placeId: String, userId: String): PlaceReview?
    suspend fun getReviews(placeId: String): PaginatedResponse<PlaceReview>
    suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReview

    suspend fun deleteReview(placeId: String, placeReview: PlaceReview)
    suspend fun bookmarkPlaceReturningCurrent(placeId: String, userId: String): Boolean
    suspend fun unbookmarkPlaceReturningCurrent(placeId: String, userId: String): Boolean
    suspend fun isPlaceBookmarkedByUser(placeId: String, userId: String): Boolean
}