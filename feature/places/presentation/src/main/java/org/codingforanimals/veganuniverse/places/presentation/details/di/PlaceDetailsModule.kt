package org.codingforanimals.veganuniverse.places.presentation.details.di


import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.BookmarkPlaceUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.DeleteUserReviewUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.GetPlaceDetailsScreenContent
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.GetPlaceDetailsUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.GetPlaceReviewsUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.SubmitReviewUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val placeDetailsModule = module {
    factoryOf(::GetPlaceDetailsScreenContent)
    factoryOf(::GetPlaceReviewsUseCase)
    factoryOf(::GetPlaceDetailsUseCase)
    factoryOf(::SubmitReviewUseCase)
    factoryOf(::DeleteUserReviewUseCase)
    factoryOf(::BookmarkPlaceUseCase)
    viewModelOf(::PlaceDetailsViewModel)
}