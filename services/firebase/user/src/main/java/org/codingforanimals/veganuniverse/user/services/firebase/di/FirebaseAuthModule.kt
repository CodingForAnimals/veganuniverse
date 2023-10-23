package org.codingforanimals.veganuniverse.user.services.firebase.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.codingforanimals.veganuniverse.user.services.firebase.AccountUpdatesManager
import org.codingforanimals.veganuniverse.user.services.firebase.Authenticator
import org.codingforanimals.veganuniverse.user.services.firebase.config.GoogleSignInWrapper
import org.codingforanimals.veganuniverse.user.services.firebase.entity.FIREBASE_USER_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.user.services.firebase.entity.mapperModule
import org.codingforanimals.veganuniverse.user.services.firebase.impl.FirebaseAccountUpdatesManager
import org.codingforanimals.veganuniverse.user.services.firebase.impl.FirebaseAuthenticator
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val userFirebaseModule = module {
    includes(
        firebaseServiceModule,
        mapperModule,
    )
    factory { Firebase.auth }
    factoryOf(::GoogleSignInWrapper)
    factoryOf(::FirebaseAccountUpdatesManager) bind AccountUpdatesManager::class
    factory<Authenticator> {
        FirebaseAuthenticator(
            googleSignInWrapper = get(),
            firebaseAuth = get(),
            firebaseUserEntityMapper = get(named(FIREBASE_USER_ENTITY_MAPPER))
        )
    }
}