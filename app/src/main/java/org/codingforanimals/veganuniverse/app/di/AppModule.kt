package org.codingforanimals.veganuniverse.app.di

import org.codingforanimals.veganuniverse.app.MainViewModel
import org.codingforanimals.veganuniverse.commons.navigation.commonsNavigationModule
import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.codingforanimals.veganuniverse.create.graph.di.createFeatureModule
import org.codingforanimals.veganuniverse.onboarding.domain.di.onboardingDomainModule
import org.codingforanimals.veganuniverse.place.presentation.di.placesFeaturePresentationModule
import org.codingforanimals.veganuniverse.product.presentation.di.productPresentationModule
import org.codingforanimals.veganuniverse.profile.di.profileFeatureModule
import org.codingforanimals.veganuniverse.recipes.presentation.recipesPresentationModule
import org.codingforanimals.veganuniverse.registration.presentation.di.registrationModule
import org.codingforanimals.veganuniverse.services.location.di.locationModule
import org.codingforanimals.veganuniverse.validator.di.validatorModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    includes(
        locationModule,
        onboardingDomainModule,
        placesFeaturePresentationModule,
        createFeatureModule,
        registrationModule,
        profileFeatureModule,
        recipesPresentationModule,
        productPresentationModule,
        commonsNavigationModule,
        userCommonDomainModule,
        validatorModule,
    )
    viewModelOf(::MainViewModel)
}