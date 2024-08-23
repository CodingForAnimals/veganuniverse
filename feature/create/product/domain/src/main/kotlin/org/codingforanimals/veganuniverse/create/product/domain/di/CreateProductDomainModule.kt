package org.codingforanimals.veganuniverse.create.product.domain.di

import org.codingforanimals.veganuniverse.auth.di.authCoreModule
import org.codingforanimals.veganuniverse.create.product.data.di.createProductDataModule
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SaveProduct
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SaveProductImpl
import org.codingforanimals.veganuniverse.create.product.domain.usecase.SubmitProduct
import org.codingforanimals.veganuniverse.product.domain.di.productDomainModule
import org.codingforanimals.veganuniverse.profile.domain.di.PROFILE_PRODUCT_USE_CASES
import org.codingforanimals.veganuniverse.profile.domain.di.profileDomainModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val createProductDomainModule = module {
    includes(
        createProductDataModule,
        productDomainModule,
        authCoreModule,
        profileDomainModule,
    )
    factoryOf(::SaveProductImpl) bind SaveProduct::class

    factory {
        SubmitProduct(
            currentUserRepository = get(),
            productRepository = get(),
            profileContentUseCases = get(named(PROFILE_PRODUCT_USE_CASES)),
            networkUtils = get(),
        )
    }
}
