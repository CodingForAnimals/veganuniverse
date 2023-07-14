package org.codingforanimals.veganuniverse.services.firebase.places.api

import org.codingforanimals.veganuniverse.services.firebase.places.model.PlaceFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.places.model.PlaceLocationQueryParams
import org.codingforanimals.veganuniverse.services.firebase.places.model.ReviewFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.places.model.ReviewsPaginatedResponse
import org.codingforanimals.veganuniverse.services.firebase.places.model.dto.ReviewDTO
import org.codingforanimals.veganuniverse.services.firebase.places.model.dto.ReviewFormDTO

interface PlacesApi {
    suspend fun fetchPlaces(params: PlaceLocationQueryParams): List<PlaceFirebaseEntity>
    suspend fun fetchReview(placeId: String, userId: String): ReviewFirebaseEntity?
    suspend fun fetchReviews(placeId: String): ReviewsPaginatedResponse
    suspend fun submitReview(placeId: String, reviewFormDTO: ReviewFormDTO): ReviewDTO
}