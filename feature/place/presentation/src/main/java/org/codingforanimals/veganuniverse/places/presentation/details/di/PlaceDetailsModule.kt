package org.codingforanimals.veganuniverse.places.presentation.details.di


import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val placeDetailsModule = module {
//    factoryOf(::GetPlaceReviewsUseCase)
//    factoryOf(::GetPlaceDetailsUseCase)
//    factoryOf(::SubmitReviewUseCase)
//    factoryOf(::DeleteUserReviewUseCase)
//    factoryOf(::BookmarkPlaceUseCase)
    viewModelOf(::PlaceDetailsViewModel)
}