package org.codingforanimals.veganuniverse.place.presentation.details.di


import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_PLACE_USE_CASES
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel
import org.codingforanimals.veganuniverse.place.presentation.details.usecase.PlaceDetailsUseCases
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val placeDetailsModule = module {
    factory {
        PlaceDetailsUseCases(
            getPlaceDetails = get(),
            getPlaceReviews = get(),
            getCurrentUserPlaceReview = get(),
            reportPlaceReview = get(),
            deletePlaceReview = get(),
            submitPlaceReview = get(),
            editPlace = get(),
            reportPlace = get(),
            profilePlaceUseCases = get(named(PROFILE_PLACE_USE_CASES)),
        )
    }
    viewModelOf(::PlaceDetailsViewModel)
}