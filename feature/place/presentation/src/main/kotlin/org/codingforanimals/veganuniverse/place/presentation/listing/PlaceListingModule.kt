package org.codingforanimals.veganuniverse.place.presentation.listing

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val placeListingModule = module {
    viewModelOf(::PlaceListingViewModel)
}