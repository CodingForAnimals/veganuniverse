package org.codingforanimals.veganuniverse.create.place.presentation.di

import org.codingforanimals.veganuniverse.create.place.domain.di.createPlaceDomainModule
import org.codingforanimals.veganuniverse.create.place.presentation.CreatePlaceViewModel
import org.codingforanimals.veganuniverse.create.place.presentation.usecase.GetAutoCompleteIntentUseCase
import org.codingforanimals.veganuniverse.create.place.presentation.usecase.GetCreatePlaceScreenContent
import org.codingforanimals.veganuniverse.create.place.presentation.usecase.GetPlaceDataUseCase
import org.codingforanimals.veganuniverse.services.google.places.googlePlacesModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val createPlacePresentationModule = module {
    includes(
        googlePlacesModule,
        createPlaceDomainModule
    )
    factoryOf(::GetAutoCompleteIntentUseCase)
    factoryOf(::GetCreatePlaceScreenContent)
    factoryOf(::GetPlaceDataUseCase)
    viewModelOf(::CreatePlaceViewModel)
}