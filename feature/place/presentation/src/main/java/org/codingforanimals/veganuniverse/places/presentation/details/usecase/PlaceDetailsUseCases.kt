package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.details.IsPlaceBookmarked
import org.codingforanimals.veganuniverse.place.details.TogglePlaceBookmark
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetPlaceReviews
import org.codingforanimals.veganuniverse.place.reviews.ReportPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.SubmitPlaceReview

data class PlaceDetailsUseCases(
    val getPlaceDetails: GetPlaceDetails,
    val getPlaceReviews: GetPlaceReviews,
    val reportPlaceReview: ReportPlaceReview,
    val deletePlaceReview: DeletePlaceReview,
    val submitPlaceReview: SubmitPlaceReview,
    val togglePlaceBookmark: TogglePlaceBookmark,
    val isPlaceBookmarked: IsPlaceBookmarked,
)
