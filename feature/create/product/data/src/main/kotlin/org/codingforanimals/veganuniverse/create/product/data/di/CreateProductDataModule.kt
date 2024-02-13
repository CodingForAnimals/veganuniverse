package org.codingforanimals.veganuniverse.create.product.data.di

import org.codingforanimals.veganuniverse.create.product.data.source.SaveProductFirestoreDataSource
import org.codingforanimals.veganuniverse.create.product.data.source.SaveProductRemoteDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.dsl.bind

val createProductDataModule = module {
    factoryOf(::SaveProductFirestoreDataSource) bind SaveProductRemoteDataSource::class
}