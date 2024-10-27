package org.codingforanimals.veganuniverse.place.di

import org.codingforanimals.veganuniverse.place.repository.PlaceRepository
import org.codingforanimals.veganuniverse.place.repository.PlaceRepositoryImpl
import org.codingforanimals.veganuniverse.place.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.place.repository.PlaceReviewRepositoryImpl
import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_PLACE_USE_CASES
import org.codingforanimals.veganuniverse.commons.profile.domain.di.profileDomainModule
import org.codingforanimals.veganuniverse.place.data.di.placeDataModule
import org.codingforanimals.veganuniverse.place.details.EditPlace
import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.details.ReportPlace
import org.codingforanimals.veganuniverse.place.home.GetPlaceAutocompleteIntent
import org.codingforanimals.veganuniverse.place.home.GetPlaceLocationData
import org.codingforanimals.veganuniverse.place.home.GetPlaces
import org.codingforanimals.veganuniverse.place.listing.usecase.QueryPlacesByIds
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetCurrentUserPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetLatestPlaceReviewsPagingFlow
import org.codingforanimals.veganuniverse.place.reviews.GetPlaceReviews
import org.codingforanimals.veganuniverse.place.reviews.ReportPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.SubmitPlaceReview
import org.codingforanimals.veganuniverse.place.usecase.GetAutoCompleteIntent
import org.codingforanimals.veganuniverse.place.usecase.GetAutoCompleteIntentImpl
import org.codingforanimals.veganuniverse.place.usecase.GetCreatePlaceScreenContent
import org.codingforanimals.veganuniverse.place.usecase.GetPlaceDataUseCase
import org.codingforanimals.veganuniverse.place.usecase.GetUnvalidatedPlacesPaginationFlowUseCase
import org.codingforanimals.veganuniverse.place.usecase.SubmitPlace
import org.codingforanimals.veganuniverse.place.usecase.ValidatePlaceUseCase
import org.codingforanimals.veganuniverse.services.google.places.googlePlacesModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val placeFeatureDomainModule = module {
    includes(
        placeDataModule,
        profileDomainModule,
        googlePlacesModule,
    )
    factory {
        SubmitPlace(
            placeRepository = get(),
            flowOnCurrentUser = get(),
            profilePlaceUseCases = get(named(PROFILE_PLACE_USE_CASES))
        )
    }
    factoryOf(::PlaceRepositoryImpl) bind PlaceRepository::class
    factoryOf(::PlaceReviewRepositoryImpl) bind PlaceReviewRepository::class

    factoryOf(::GetPlaceDataUseCase)
    factoryOf(::GetCreatePlaceScreenContent)
    factoryOf(::GetAutoCompleteIntentImpl) bind GetAutoCompleteIntent::class
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
    factoryOf(::GetUnvalidatedPlacesPaginationFlowUseCase)
    factoryOf(::ValidatePlaceUseCase)
}
