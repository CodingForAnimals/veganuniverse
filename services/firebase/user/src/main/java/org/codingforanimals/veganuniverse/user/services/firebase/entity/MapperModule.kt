package org.codingforanimals.veganuniverse.user.services.firebase.entity

import com.google.firebase.auth.FirebaseUser
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.user.services.firebase.model.UserFirebaseEntity
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val mapperModule = module {
    factory<OneWayEntityMapper<FirebaseUser, UserFirebaseEntity>>(
        named(FIREBASE_USER_ENTITY_MAPPER)
    ) { FirebaseUserEntityMapper(storageBucketWrapper = get()) }
}

internal const val FIREBASE_USER_ENTITY_MAPPER = "firebase-user-entity-mapper"