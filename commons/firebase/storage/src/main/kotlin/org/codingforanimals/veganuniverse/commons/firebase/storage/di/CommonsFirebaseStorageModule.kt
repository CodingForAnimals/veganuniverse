package org.codingforanimals.veganuniverse.commons.firebase.storage.di

import org.codingforanimals.veganuniverse.commons.firebase.storage.PublicImageApi
import org.codingforanimals.veganuniverse.commons.firebase.storage.StorageBucketWrapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val commonsFirebaseStorageModule = module {
    factoryOf(::StorageBucketWrapper)
    factory {
        val storageBucketWrapper = get<StorageBucketWrapper>()
        PublicImageApi(
            basePath = "${PublicImageApi.PUBLIC_IMAGE_BASE_PATH}/${storageBucketWrapper.storageBucket}"
        )
    }
}