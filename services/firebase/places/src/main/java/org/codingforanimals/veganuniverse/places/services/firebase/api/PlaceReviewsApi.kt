package org.codingforanimals.veganuniverse.places.services.firebase.api

import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.entity.ReviewsPaginatedResponse

interface PlaceReviewsApi {
    suspend fun fetchReview(placeId: String, userId: String): PlaceReview?
    suspend fun fetchReviews(placeId: String): ReviewsPaginatedResponse
    suspend fun submitReview(placeId: String, placeReviewForm: PlaceReviewForm): PlaceReview
    suspend fun deleteReview(placeId: String, reviewId: String)
}