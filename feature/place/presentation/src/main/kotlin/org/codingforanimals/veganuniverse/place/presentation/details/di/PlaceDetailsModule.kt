package org.codingforanimals.veganuniverse.place.presentation.details.di


import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel
import org.codingforanimals.veganuniverse.place.presentation.details.usecase.PlaceDetailsUseCases
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val placeDetailsModule = module {
    factoryOf(::PlaceDetailsUseCases)
    viewModelOf(::PlaceDetailsViewModel)
}