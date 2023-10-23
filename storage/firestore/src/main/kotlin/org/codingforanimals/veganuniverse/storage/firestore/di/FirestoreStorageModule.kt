package org.codingforanimals.veganuniverse.storage.firestore.di

import org.codingforanimals.veganuniverse.storage.firestore.DocumentSnapshotCache
import org.codingforanimals.veganuniverse.storage.firestore.DocumentSnapshotLruCache
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firestoreStorageModule = module {
    singleOf(::DocumentSnapshotLruCache) bind DocumentSnapshotCache::class
}