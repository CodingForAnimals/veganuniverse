package org.codingforanimals.veganuniverse.user.services.firebase.di

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.codingforanimals.veganuniverse.user.services.firebase.AccountUpdatesManager
import org.codingforanimals.veganuniverse.user.services.firebase.Authenticator
import org.codingforanimals.veganuniverse.user.services.firebase.FirebaseAccountUpdatesManager
import org.codingforanimals.veganuniverse.user.services.firebase.FirebaseAuthenticator
import org.codingforanimals.veganuniverse.user.services.firebase.GoogleSignInWrapper
import org.codingforanimals.veganuniverse.user.services.firebase.entity.FirebaseUserEntityMapper
import org.codingforanimals.veganuniverse.user.services.firebase.model.UserFirebaseEntity
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val userFirebaseModule = module {
    includes(firebaseServiceModule)
    factory { Firebase.auth }
    factory<OneWayEntityMapper<FirebaseUser, UserFirebaseEntity>>(
        named(FirebaseUserEntityMapper::class.java.simpleName)
    ) { FirebaseUserEntityMapper(storageBucketWrapper = get()) }
    factoryOf(::GoogleSignInWrapper)
    factoryOf(::FirebaseAccountUpdatesManager) bind AccountUpdatesManager::class
    factory<Authenticator> {
        FirebaseAuthenticator(
            googleSignInWrapper = get(),
            firebaseAuth = get(),
            firebaseUserEntityMapper = get(named(FirebaseUserEntityMapper::class.java.simpleName))
        )
    }
}