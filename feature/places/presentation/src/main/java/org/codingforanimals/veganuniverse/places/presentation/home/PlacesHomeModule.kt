package org.codingforanimals.veganuniverse.places.presentation.home

import org.codingforanimals.veganuniverse.places.presentation.home.state.PlacesHomeSavedStateHandler
import org.codingforanimals.veganuniverse.places.presentation.home.usecase.GetAutocompleteIntent
import org.codingforanimals.veganuniverse.places.presentation.home.usecase.GetLocationDataUseCase
import org.codingforanimals.veganuniverse.places.presentation.home.usecase.GetPlacesUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val placesHomeModule = module {
    factoryOf(::GetPlacesUseCase)
    factoryOf(::GetAutocompleteIntent)
    factoryOf(::GetLocationDataUseCase)
    singleOf(::PlacesHomeSavedStateHandler)
    viewModelOf(::PlacesHomeViewModel)
}