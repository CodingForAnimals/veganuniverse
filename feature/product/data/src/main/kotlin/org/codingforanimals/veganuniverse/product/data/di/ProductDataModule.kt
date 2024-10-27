package org.codingforanimals.veganuniverse.product.data.di

import org.codingforanimals.veganuniverse.firebase.storage.di.firebaseStorageModule
import org.codingforanimals.veganuniverse.product.data.config.local.di.productConfigLocalModule
import org.codingforanimals.veganuniverse.product.data.config.remote.di.productConfigRemoteModule
import org.codingforanimals.veganuniverse.product.data.source.local.di.productLocalDataModule
import org.codingforanimals.veganuniverse.product.data.source.remote.di.productRemoteDataModule
import org.koin.dsl.module

val productDataModule = module {
    includes(
        firebaseStorageModule,
        productRemoteDataModule,
        productLocalDataModule,
        productConfigLocalModule,
        productConfigRemoteModule,
    )
}
