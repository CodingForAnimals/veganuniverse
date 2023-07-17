package org.codingforanimals.veganuniverse.auth.di

import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.UserRepositoryImpl
import org.codingforanimals.veganuniverse.services.firebase.auth.di.firebaseAuthModule
import org.koin.dsl.module

val authModule = module {
    includes(firebaseAuthModule)
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
}