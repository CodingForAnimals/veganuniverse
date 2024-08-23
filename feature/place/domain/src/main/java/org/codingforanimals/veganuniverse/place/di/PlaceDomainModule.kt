package org.codingforanimals.veganuniverse.place.di

import org.codingforanimals.veganuniverse.place.reviews.GetPlaceReviews
import org.codingforanimals.veganuniverse.place.home.GetPlaceAutocompleteIntent
import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.home.GetPlaceLocationData
import org.codingforanimals.veganuniverse.place.home.GetPlaces
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetLatestPlaceReviewsPagingFlow
import org.codingforanimals.veganuniverse.place.reviews.ReportPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.SubmitPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetCurrentUserPlaceReview
import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_PLACE_USE_CASES
import org.codingforanimals.veganuniverse.commons.profile.domain.di.profileDomainModule
import org.codingforanimals.veganuniverse.place.details.EditPlace
import org.codingforanimals.veganuniverse.place.details.ReportPlace
import org.codingforanimals.veganuniverse.place.listing.usecase.QueryPlacesByIds
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val placeFeatureDomainModule = module {
    includes(
        profileDomainModule,
    )
    factoryOf(::GetPlaceAutocompleteIntent)
    factoryOf(::GetPlaceDetails)
    factoryOf(::GetPlaceLocationData)
    factoryOf(::GetPlaces)
    factoryOf(::GetPlaceReviews)
    factoryOf(::GetLatestPlaceReviewsPagingFlow)
    factoryOf(::ReportPlaceReview)
    factoryOf(::DeletePlaceReview)
    factoryOf(::SubmitPlaceReview)
    factoryOf(::GetCurrentUserPlaceReview)
    factoryOf(::ReportPlace)
    factoryOf(::EditPlace)
    factoryOf(::QueryPlacesByIds)
}
