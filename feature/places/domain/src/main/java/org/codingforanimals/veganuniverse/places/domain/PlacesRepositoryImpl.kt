package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.PaginatedResponse
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceCard
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.services.firebase.api.PlaceReviewsApi
import org.codingforanimals.veganuniverse.services.firebase.api.PlacesApi

class PlacesRepositoryImpl(
    private val placesApi: PlacesApi,
    private val placeReviewsApi: PlaceReviewsApi,
) : PlacesRepository {

    override suspend fun getPlaces(params: GeoLocationQueryParams): List<PlaceCard> {
        return placesApi.fetchPlaces(params)
    }

    override suspend fun getPlace(id: String): Place? {
        return placesApi.fetchPlace(id)
    }

    override suspend fun getReview(placeId: String, userId: String): PlaceReview? {
        return placeReviewsApi.fetchReview(placeId, userId)
    }

    override suspend fun getReviews(placeId: String): PaginatedResponse<PlaceReview> {
        val response = placeReviewsApi.fetchReviews(placeId)
        return PaginatedResponse(
            content = response.content,
            hasMoreItems = response.hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReview {
        return placeReviewsApi.submitReview(placeId, placeReviewForm)
    }

    override suspend fun deleteReview(placeId: String, placeReview: PlaceReview) {
        placeReviewsApi.deleteReview(placeId, placeReview)
    }
}