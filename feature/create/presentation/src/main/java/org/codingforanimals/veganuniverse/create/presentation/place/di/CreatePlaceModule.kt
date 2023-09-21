package org.codingforanimals.veganuniverse.create.presentation.place.di

import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetAutoCompleteIntentUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetCreatePlaceScreenContent
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.SendVerificationEmailUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.SubmitPlaceUseCase
import org.codingforanimals.veganuniverse.services.google.places.googlePlacesModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val createPlacePresentationModule = module {
    includes(googlePlacesModule)
    factoryOf(::GetAutoCompleteIntentUseCase)
    factoryOf(::SendVerificationEmailUseCase)
    factoryOf(::GetCreatePlaceScreenContent)
    factoryOf(::GetPlaceDataUseCase)
    factoryOf(::SubmitPlaceUseCase)
    viewModelOf(::CreatePlaceViewModel)
}