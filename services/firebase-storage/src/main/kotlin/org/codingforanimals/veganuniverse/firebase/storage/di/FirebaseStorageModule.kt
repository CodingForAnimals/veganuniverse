package org.codingforanimals.veganuniverse.firebase.storage.di

import com.google.firebase.storage.FirebaseStorage
import org.codingforanimals.veganuniverse.firebase.storage.model.PublicImageApi
import org.codingforanimals.veganuniverse.firebase.storage.model.StorageBucketWrapper
import org.codingforanimals.veganuniverse.firebase.storage.usecase.UploadPictureUseCase
import org.codingforanimals.veganuniverse.firebase.storage.usecase.UploadPictureUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val firebaseStorageModule = module {
    factoryOf(::StorageBucketWrapper)
    factory(named(KOIN_STORAGE_BUCKET)) {
        val storageBucketWrapper: StorageBucketWrapper = get()
        storageBucketWrapper.storageBucket
    }
    factory {
        val storageBucket: String = get(named(KOIN_STORAGE_BUCKET))
        PublicImageApi(
            storageBucket = storageBucket
        )
    }

    factory<UploadPictureUseCase> {
        UploadPictureUseCaseImpl(
            storage = FirebaseStorage.getInstance(),
        )
    }
}

const val KOIN_STORAGE_BUCKET = "storage-bucket"