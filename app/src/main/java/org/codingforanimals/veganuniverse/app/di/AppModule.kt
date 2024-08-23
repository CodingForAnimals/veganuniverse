package org.codingforanimals.veganuniverse.app.di

import org.codingforanimals.veganuniverse.app.MainViewModel
import org.codingforanimals.veganuniverse.auth.di.authCoreModule
import org.codingforanimals.veganuniverse.common.di.commonModule
import org.codingforanimals.veganuniverse.core.location.di.locationModule
import org.codingforanimals.veganuniverse.create.graph.di.createFeatureModule
import org.codingforanimals.veganuniverse.onboarding.presentation.di.onboardingModule
import org.codingforanimals.veganuniverse.places.presentation.di.placesFeaturePresentationModule
import org.codingforanimals.veganuniverse.product.graph.di.productFeatureModule
import org.codingforanimals.veganuniverse.profile.di.profileFeatureModule
import org.codingforanimals.veganuniverse.recipes.presentation.recipesPresentationModule
import org.codingforanimals.veganuniverse.registration.presentation.di.registrationModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    includes(
        commonModule,
        locationModule,
        onboardingModule,
        authCoreModule,
        placesFeaturePresentationModule,
        createFeatureModule,
        registrationModule,
        profileFeatureModule,
        recipesPresentationModule,
        productFeatureModule,
    )
    viewModelOf(::MainViewModel)
}