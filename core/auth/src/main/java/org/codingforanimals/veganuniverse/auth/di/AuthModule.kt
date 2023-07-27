package org.codingforanimals.veganuniverse.auth.di

import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.UserRepositoryImpl
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatusImpl
import org.codingforanimals.veganuniverse.services.firebase.auth.di.firebaseAuthModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    includes(firebaseAuthModule)
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    factoryOf(::GetUserStatusImpl) bind GetUserStatus::class
}