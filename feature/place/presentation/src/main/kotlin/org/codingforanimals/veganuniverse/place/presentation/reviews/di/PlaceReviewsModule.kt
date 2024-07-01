package org.codingforanimals.veganuniverse.place.presentation.reviews.di

import org.codingforanimals.veganuniverse.place.presentation.reviews.PlaceReviewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val placeReviewsModule = module {
    viewModelOf(::PlaceReviewsViewModel)
}