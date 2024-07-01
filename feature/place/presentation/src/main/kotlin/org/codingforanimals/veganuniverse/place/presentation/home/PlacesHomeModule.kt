package org.codingforanimals.veganuniverse.place.presentation.home

import org.codingforanimals.veganuniverse.place.home.GetPlaces
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val placesHomeModule = module {
    factoryOf(::GetPlaces)
    viewModelOf(::PlacesHomeViewModel)
}