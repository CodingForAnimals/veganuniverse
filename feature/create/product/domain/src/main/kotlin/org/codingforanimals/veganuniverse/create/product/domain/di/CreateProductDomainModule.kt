package org.codingforanimals.veganuniverse.create.product.domain.di

import org.codingforanimals.veganuniverse.create.product.domain.usecase.SubmitProduct
import org.codingforanimals.veganuniverse.commons.product.domain.di.productCommonDomainModule
import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_PRODUCT_USE_CASES
import org.codingforanimals.veganuniverse.commons.profile.domain.di.profileDomainModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val createProductDomainModule = module {
    includes(
        productCommonDomainModule,
        profileDomainModule,
    )

    factory {
        SubmitProduct(
            productRepository = get(),
            profileProductUseCases = get(named(PROFILE_PRODUCT_USE_CASES)),
            flowOnCurrentUser = get(),
        )
    }
}
