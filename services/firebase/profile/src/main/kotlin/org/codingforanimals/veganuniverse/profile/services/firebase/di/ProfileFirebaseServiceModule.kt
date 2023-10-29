package org.codingforanimals.veganuniverse.profile.services.firebase.di

import android.util.LruCache
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.profile.services.firebase.impl.ProfileLookupsFirebaseService
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileFirebaseServiceModule = module {
    includes(
        firebaseServiceModule
    )
    single { LruCache<String, List<String>>(4 * 1024 * 1024) }
    factoryOf(::ProfileLookupsFirebaseService) bind ProfileLookupsService::class
}