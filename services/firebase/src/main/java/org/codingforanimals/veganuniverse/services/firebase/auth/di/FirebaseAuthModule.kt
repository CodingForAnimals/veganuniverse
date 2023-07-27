package org.codingforanimals.veganuniverse.services.firebase.auth.di

import org.codingforanimals.veganuniverse.services.firebase.auth.Authenticator
import org.codingforanimals.veganuniverse.services.firebase.auth.FirebaseAuthenticator
import org.codingforanimals.veganuniverse.services.firebase.auth.GoogleSignInWrapper
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseAuthModule = module {
    includes(firebaseServiceModule)
    factoryOf(::GoogleSignInWrapper)
    factoryOf(::FirebaseAuthenticator) bind Authenticator::class
}