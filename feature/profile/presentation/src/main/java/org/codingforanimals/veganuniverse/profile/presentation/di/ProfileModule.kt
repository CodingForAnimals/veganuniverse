package org.codingforanimals.veganuniverse.profile.presentation.di

import org.codingforanimals.veganuniverse.auth.di.authCoreModule
import org.codingforanimals.veganuniverse.profile.domain.profileDomainModule
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel
import org.codingforanimals.veganuniverse.profile.presentation.usecase.GetUserFeatureContributionsUseCase
import org.codingforanimals.veganuniverse.profile.presentation.usecase.LogoutUseCase
import org.codingforanimals.veganuniverse.profile.presentation.usecase.UploadNewProfilePictureUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profilePresentationModule = module {
    includes(authCoreModule, profileDomainModule)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetUserFeatureContributionsUseCase)
    factoryOf(::UploadNewProfilePictureUseCase)
    viewModelOf(::ProfileScreenViewModel)
}