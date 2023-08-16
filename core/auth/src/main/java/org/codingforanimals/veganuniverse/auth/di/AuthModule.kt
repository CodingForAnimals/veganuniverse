package org.codingforanimals.veganuniverse.auth.di

import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.UserRepositoryImpl
import org.codingforanimals.veganuniverse.auth.services.firebase.di.authFirebaseModule
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatusImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authCoreModule = module {
    includes(authFirebaseModule)
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    factoryOf(::GetUserStatusImpl) bind GetUserStatus::class
}