package org.codingforanimals.veganuniverse.place.presentation.di

import org.codingforanimals.veganuniverse.place.di.placeFeatureDomainModule
import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.presentation.details.di.placeDetailsModule
import org.codingforanimals.veganuniverse.place.presentation.home.placesHomeModule
import org.codingforanimals.veganuniverse.place.presentation.reviews.di.placeReviewsModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val placesFeaturePresentationModule = module {
    includes(
        placeFeatureDomainModule,
        placesHomeModule,
        placeDetailsModule,
        placeReviewsModule,
    )
    factoryOf(::GetPlaceDetails)
}