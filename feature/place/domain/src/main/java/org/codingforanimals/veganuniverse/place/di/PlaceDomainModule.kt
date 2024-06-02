package org.codingforanimals.veganuniverse.place.di

import org.codingforanimals.veganuniverse.place.reviews.GetPlaceReviews
import org.codingforanimals.veganuniverse.place.home.GetPlaceAutocompleteIntent
import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.details.IsPlaceBookmarked
import org.codingforanimals.veganuniverse.place.home.GetPlaceLocationData
import org.codingforanimals.veganuniverse.place.home.GetPlaces
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetLatestPlaceReviewsPagingFlow
import org.codingforanimals.veganuniverse.place.reviews.ReportPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.SubmitPlaceReview
import org.codingforanimals.veganuniverse.place.details.TogglePlaceBookmark
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val placeFeatureDomainModule = module {
    factoryOf(::GetPlaceAutocompleteIntent)
    factoryOf(::GetPlaceDetails)
    factoryOf(::GetPlaceLocationData)
    factoryOf(::GetPlaces)
    factoryOf(::GetPlaceReviews)
    factoryOf(::GetLatestPlaceReviewsPagingFlow)
    factoryOf(::ReportPlaceReview)
    factoryOf(::DeletePlaceReview)
    factoryOf(::SubmitPlaceReview)
    factoryOf(::TogglePlaceBookmark)
    factoryOf(::IsPlaceBookmarked)
}
