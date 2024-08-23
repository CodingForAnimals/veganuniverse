package org.codingforanimals.veganuniverse.place.presentation.details.usecase

import org.codingforanimals.veganuniverse.commons.user.domain.usecase.ReauthenticationUseCases
import org.codingforanimals.veganuniverse.place.details.EditPlace
import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.details.IsPlaceBookmarked
import org.codingforanimals.veganuniverse.place.details.ReportPlace
import org.codingforanimals.veganuniverse.place.details.TogglePlaceBookmark
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetCurrentUserPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetPlaceReviews
import org.codingforanimals.veganuniverse.place.reviews.ReportPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.SubmitPlaceReview

data class PlaceDetailsUseCases(
    val getPlaceDetails: GetPlaceDetails,
    val getPlaceReviews: GetPlaceReviews,
    val getCurrentUserPlaceReview: GetCurrentUserPlaceReview,
    val reportPlaceReview: ReportPlaceReview,
    val deletePlaceReview: DeletePlaceReview,
    val submitPlaceReview: SubmitPlaceReview,
    val togglePlaceBookmark: TogglePlaceBookmark,
    val isPlaceBookmarked: IsPlaceBookmarked,
    val reauthenticationUseCases: ReauthenticationUseCases,
    val reportPlace: ReportPlace,
    val editPlace: EditPlace,
)
