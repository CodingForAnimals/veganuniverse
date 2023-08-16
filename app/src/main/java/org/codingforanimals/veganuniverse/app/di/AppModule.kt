package org.codingforanimals.veganuniverse.app.di

import org.codingforanimals.places.presentation.di.placesFeatureModule
import org.codingforanimals.veganuniverse.app.MainViewModel
import org.codingforanimals.veganuniverse.auth.di.authCoreModule
import org.codingforanimals.veganuniverse.common.di.commonModule
import org.codingforanimals.veganuniverse.core.location.di.locationModule
import org.codingforanimals.veganuniverse.create.presentation.di.createFeatureModule
import org.codingforanimals.veganuniverse.onboarding.presentation.di.onboardingModule
import org.codingforanimals.veganuniverse.profile.presentation.di.profileModule
import org.codingforanimals.veganuniverse.registration.presentation.di.registrationModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    includes(
        commonModule,
        locationModule,
        onboardingModule,
        authCoreModule,
        placesFeatureModule,
        createFeatureModule,
        registrationModule,
        profileModule,
    )
    viewModelOf(::MainViewModel)
}