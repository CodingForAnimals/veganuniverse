package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.entity.ReviewsPaginatedResponse

interface PlacesRepository {
    suspend fun getPlaces(params: GeoLocationQueryParams): List<Place>
    fun getPlace(id: String): Place
    suspend fun getReview(placeId: String, userId: String): PlaceReview?
    suspend fun getReviews(placeId: String): ReviewsPaginatedResponse
    suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReview

    suspend fun deleteReview(placeId: String, reviewId: String)
}