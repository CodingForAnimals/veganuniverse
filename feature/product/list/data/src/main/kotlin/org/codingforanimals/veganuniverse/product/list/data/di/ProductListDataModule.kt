package org.codingforanimals.veganuniverse.product.list.data.di

import androidx.paging.PagingConfig
import org.codingforanimals.veganuniverse.commons.firebase.storage.di.commonsFirebaseStorageModule
import org.codingforanimals.veganuniverse.product.list.data.source.GetProductsRemoteDataSource
import org.codingforanimals.veganuniverse.product.list.data.source.GetProductFirestoreDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productListDataModule = module {
    includes(commonsFirebaseStorageModule)
    factoryOf(::GetProductFirestoreDataSource) bind GetProductsRemoteDataSource::class
    factory { PagingConfig(3) }
}
