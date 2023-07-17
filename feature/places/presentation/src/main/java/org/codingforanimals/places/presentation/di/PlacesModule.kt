package org.codingforanimals.places.presentation.di

import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel
import org.codingforanimals.places.presentation.details.usecase.DeleteUserReviewUseCase
import org.codingforanimals.places.presentation.details.usecase.GetPlaceDetailsScreenContent
import org.codingforanimals.places.presentation.details.usecase.GetPlaceDetailsUseCase
import org.codingforanimals.places.presentation.details.usecase.GetPlaceReviewsUseCase
import org.codingforanimals.places.presentation.details.usecase.SubmitReviewUseCase
import org.codingforanimals.places.presentation.home.PlacesHomeViewModel
import org.codingforanimals.places.presentation.home.state.PlacesHomeSavedStateHandler
import org.codingforanimals.places.presentation.home.usecase.GetPlacesUseCase
import org.codingforanimals.veganuniverse.places.domain.di.placesDomainModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val placesFeatureModule = module {
    includes(placesDomainModule)
    factoryOf(::GetPlacesUseCase)
    singleOf(::PlacesHomeSavedStateHandler)
    viewModelOf(::PlacesHomeViewModel)

    factoryOf(::GetPlaceDetailsScreenContent)
    factoryOf(::GetPlaceReviewsUseCase)
    factoryOf(::GetPlaceDetailsUseCase)
    factoryOf(::SubmitReviewUseCase)
    factoryOf(::DeleteUserReviewUseCase)
    viewModelOf(::PlaceDetailsViewModel)
}