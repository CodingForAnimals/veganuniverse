package org.codingforanimals.veganuniverse.create.product.domain.di

import org.codingforanimals.veganuniverse.auth.di.authCoreModule
import org.codingforanimals.veganuniverse.create.product.data.di.createProductDataModule
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SaveProduct
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SaveProductImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val createProductDomainModule = module {
    includes(
        createProductDataModule,
        authCoreModule,
    )
    factoryOf(::SaveProductImpl) bind SaveProduct::class
}
