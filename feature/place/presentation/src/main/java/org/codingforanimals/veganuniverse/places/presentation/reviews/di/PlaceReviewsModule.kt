package org.codingforanimals.veganuniverse.places.presentation.reviews.di

import org.codingforanimals.veganuniverse.places.presentation.reviews.PlaceReviewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val placeReviewsModule = module {
    viewModelOf(::PlaceReviewsViewModel)
}