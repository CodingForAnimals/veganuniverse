package org.codingforanimals.veganuniverse.user.domain.di

import org.codingforanimals.veganuniverse.user.data.di.userDataModule
import org.codingforanimals.veganuniverse.user.domain.repository.CurrentCurrentUserRepositoryImpl
import org.codingforanimals.veganuniverse.user.domain.repository.CurrentUserRepository
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUserImpl
import org.codingforanimals.veganuniverse.user.domain.usecase.GetUser
import org.codingforanimals.veganuniverse.user.domain.usecase.GetUserImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userDomainModule = module {
    includes(
        userDataModule,
    )
    factoryOf(::CurrentCurrentUserRepositoryImpl) bind CurrentUserRepository::class
    factoryOf(::GetCurrentUserImpl) bind GetCurrentUser::class
    factoryOf(::GetUserImpl) bind GetUser::class
}