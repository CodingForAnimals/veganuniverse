package org.codingforanimals.veganuniverse.places.domain

import org.codingforanimals.veganuniverse.entity.PaginatedResponse
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceCard
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.services.firebase.PlaceReviewsService
import org.codingforanimals.veganuniverse.places.services.firebase.PlacesService

class PlacesRepositoryImpl(
    private val placesService: PlacesService,
    private val placeReviewsService: PlaceReviewsService,
) : PlacesRepository {

    override suspend fun getPlaces(params: GeoLocationQueryParams): List<PlaceCard> {
        return placesService.fetchPlacesCards(params)
    }

    override suspend fun getPlace(id: String): Place? {
        return placesService.fetchPlace(id)
    }

    override suspend fun getReview(placeId: String, userId: String): PlaceReview? {
        return placeReviewsService.fetchReview(placeId, userId)
    }

    override suspend fun getReviews(placeId: String): PaginatedResponse<PlaceReview> {
        val response = placeReviewsService.fetchReviews(placeId)
        return PaginatedResponse(
            content = response.content,
            hasMoreItems = response.hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReview {
        return placeReviewsService.submitReview(placeId, placeReviewForm)
    }

    override suspend fun deleteReview(placeId: String, placeReview: PlaceReview) {
        placeReviewsService.deleteReview(placeId, placeReview)
    }
}