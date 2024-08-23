package org.codingforanimals.veganuniverse.commons.product.domain.di

import org.codingforanimals.veganuniverse.commons.product.data.di.productCommonDataModule
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productCommonDomainModule = module {
    includes(productCommonDataModule)
    factoryOf(::ProductRepositoryImpl) bind ProductRepository::class
}
