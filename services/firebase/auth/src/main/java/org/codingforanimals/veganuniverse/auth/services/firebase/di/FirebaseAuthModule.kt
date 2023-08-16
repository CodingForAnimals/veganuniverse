package org.codingforanimals.veganuniverse.auth.services.firebase.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.auth.services.firebase.Authenticator
import org.codingforanimals.veganuniverse.auth.services.firebase.FirebaseAuthenticator
import org.codingforanimals.veganuniverse.auth.services.firebase.GoogleSignInWrapper
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authFirebaseModule = module {
    includes(firebaseServiceModule)
    factory { Firebase.auth }
    factoryOf(::GoogleSignInWrapper)
    factoryOf(::FirebaseAuthenticator) bind Authenticator::class
}