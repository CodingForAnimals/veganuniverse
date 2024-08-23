package org.codingforanimals.veganuniverse.product.domain.di

import org.codingforanimals.veganuniverse.product.data.di.productDataModule
import org.codingforanimals.veganuniverse.product.domain.ProductRepository
import org.codingforanimals.veganuniverse.product.domain.ProductRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productDomainModule = module {
    includes(productDataModule)
    factoryOf(::ProductRepositoryImpl) bind ProductRepository::class
}
