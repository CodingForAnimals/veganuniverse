package org.codingforanimals.veganuniverse.product.presentation.di

import org.codingforanimals.veganuniverse.commons.product.domain.di.productCommonDomainModule
import org.codingforanimals.veganuniverse.product.domain.di.productFeatureDomainModule
import org.codingforanimals.veganuniverse.product.presentation.components.ProductAdditionalInfoViewModel
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingViewModel
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.codingforanimals.veganuniverse.commons.user.presentation.di.userCommonPresentationModule
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingUseCases
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val productPresentationModule = module {
    includes(
        productFeatureDomainModule,
        userCommonDomainModule,
        userCommonPresentationModule,
        productCommonDomainModule,
    )
    factoryOf(::ProductHomeUseCases)
    factoryOf(::ProductBrowsingUseCases)
    viewModelOf(::ProductHomeViewModel)
    viewModelOf(::ProductBrowsingViewModel)
    viewModelOf(::ProductAdditionalInfoViewModel)
}
