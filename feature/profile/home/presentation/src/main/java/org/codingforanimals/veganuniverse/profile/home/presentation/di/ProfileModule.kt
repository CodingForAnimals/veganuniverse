package org.codingforanimals.veganuniverse.profile.home.presentation.di

import org.codingforanimals.veganuniverse.auth.di.authCoreModule
import org.codingforanimals.veganuniverse.profile.home.domain.di.profileDomainModule
import org.codingforanimals.veganuniverse.profile.home.presentation.ProfileScreenViewModel
import org.codingforanimals.veganuniverse.profile.home.presentation.usecase.GetBookmarksUseCase
import org.codingforanimals.veganuniverse.profile.home.presentation.usecase.GetContributionsUseCase
import org.codingforanimals.veganuniverse.profile.home.presentation.usecase.LogoutUseCase
import org.codingforanimals.veganuniverse.profile.home.presentation.usecase.UploadNewProfilePictureUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileHomePresentationModule = module {
    includes(authCoreModule, profileDomainModule)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetContributionsUseCase)
    factoryOf(::UploadNewProfilePictureUseCase)
    factoryOf(::GetBookmarksUseCase)
    viewModelOf(::ProfileScreenViewModel)
}