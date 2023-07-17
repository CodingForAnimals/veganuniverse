package org.codingforanimals.veganuniverse.services.firebase.auth.di

import org.codingforanimals.veganuniverse.services.firebase.auth.EmailAuthenticator
import org.codingforanimals.veganuniverse.services.firebase.auth.FirebaseEmailAuthenticator
import org.codingforanimals.veganuniverse.services.firebase.auth.GmailAuthenticator
import org.codingforanimals.veganuniverse.services.firebase.auth.GoogleSignInWrapper
import org.codingforanimals.veganuniverse.services.firebase.auth.LogoutUseCase
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseAuthModule = module {
    includes(firebaseServiceModule)
    factoryOf(::GmailAuthenticator)
    factoryOf(::GoogleSignInWrapper)
    factoryOf(::LogoutUseCase)
    factoryOf(::FirebaseEmailAuthenticator) bind EmailAuthenticator::class
}