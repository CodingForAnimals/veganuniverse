package org.codingforanimals.veganuniverse.place.presentation.validate

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val validatePlacesModule = module {
    viewModelOf(::ValidatePlacesViewModel)
}