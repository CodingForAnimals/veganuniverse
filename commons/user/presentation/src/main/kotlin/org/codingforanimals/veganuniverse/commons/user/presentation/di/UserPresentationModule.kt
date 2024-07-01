package org.codingforanimals.veganuniverse.commons.user.presentation.di

import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val userCommonPresentationModule = module {
    includes(userCommonDomainModule)
    viewModelOf(::UnverifiedEmailViewModel)
}