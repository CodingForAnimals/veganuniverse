package org.codingforanimals.veganuniverse.validator.place.di

import org.codingforanimals.veganuniverse.validator.place.domain.GetUnvalidatedPlacesPaginationFlowUseCase
import org.codingforanimals.veganuniverse.validator.place.domain.ValidatePlaceUseCase
import org.codingforanimals.veganuniverse.validator.place.presentation.ValidatePlacesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val placeModule = module {
    factoryOf(::GetUnvalidatedPlacesPaginationFlowUseCase)
    factoryOf(::ValidatePlaceUseCase)
    viewModelOf(::ValidatePlacesViewModel)
}
