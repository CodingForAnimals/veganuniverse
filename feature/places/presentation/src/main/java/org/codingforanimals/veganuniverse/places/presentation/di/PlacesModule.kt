package org.codingforanimals.veganuniverse.places.presentation.di

import org.codingforanimals.veganuniverse.places.domain.di.placesDomainModule
import org.codingforanimals.veganuniverse.places.presentation.details.di.placeDetailsModule
import org.codingforanimals.veganuniverse.places.presentation.home.placesHomeModule
import org.koin.dsl.module

val placesFeaturePresentationModule = module {
    includes(
        placesDomainModule,
        placesHomeModule,
        placeDetailsModule,
    )
}