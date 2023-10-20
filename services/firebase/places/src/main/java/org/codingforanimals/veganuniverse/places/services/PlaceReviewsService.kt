package org.codingforanimals.veganuniverse.places.services

import org.codingforanimals.veganuniverse.entity.PaginatedResponse
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm

interface PlaceReviewsService {
    suspend fun fetchReview(placeId: String, userId: String): PlaceReview?
    suspend fun fetchReviews(placeId: String): PaginatedResponse<PlaceReview>
    suspend fun submitReview(placeId: String, placeReviewForm: PlaceReviewForm): PlaceReview
    suspend fun deleteReview(placeId: String, placeReview: PlaceReview)
}