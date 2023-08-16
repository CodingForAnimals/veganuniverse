package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.entity.ReviewsPaginatedResponse
import org.codingforanimals.veganuniverse.places.services.firebase.api.PlaceReviewsApi
import org.codingforanimals.veganuniverse.places.services.firebase.api.PlacesApi

class PlacesRepositoryImpl(
    private val placesApi: PlacesApi,
    private val placeReviewsApi: PlaceReviewsApi,
    private val currentPlacesWrapper: CurrentPlacesWrapper,
) : PlacesRepository {

    override suspend fun getPlaces(params: GeoLocationQueryParams): List<Place> {
        val places = placesApi.fetchPlaces(params)
        currentPlacesWrapper.currentPlaces = places
        return places
    }

    override fun getPlace(id: String): Place {
        return currentPlacesWrapper.currentPlaces.first { it.id == id }
    }

    override suspend fun getReview(placeId: String, userId: String): PlaceReview? {
        return placeReviewsApi.fetchReview(placeId, userId)
    }

    override suspend fun getReviews(placeId: String): ReviewsPaginatedResponse {
        val response = placeReviewsApi.fetchReviews(placeId)
        return ReviewsPaginatedResponse(
            reviews = response.reviews,
            hasMoreItems = response.hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReview {
        return placeReviewsApi.submitReview(placeId, placeReviewForm)
    }

    override suspend fun deleteReview(placeId: String, reviewId: String) {
        placeReviewsApi.deleteReview(placeId, reviewId)
    }
}

class CurrentPlacesWrapper {
    var currentPlaces: List<Place> = emptyList()
}