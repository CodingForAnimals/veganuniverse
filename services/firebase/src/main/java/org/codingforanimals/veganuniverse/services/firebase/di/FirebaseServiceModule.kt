package org.codingforanimals.veganuniverse.services.firebase.di

import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.codingforanimals.veganuniverse.services.firebase.StorageBucketWrapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val firebaseServiceModule = module {
    factory { Firebase.firestore }
    factory { Firebase.database }
    factory { Firebase.storage }
    factoryOf(::StorageBucketWrapper)
}