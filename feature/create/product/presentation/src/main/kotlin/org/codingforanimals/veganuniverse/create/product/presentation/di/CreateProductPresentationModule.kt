package org.codingforanimals.veganuniverse.create.product.presentation.di

import org.codingforanimals.veganuniverse.create.product.domain.di.createProductDomainModule
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel
import org.codingforanimals.veganuniverse.commons.product.domain.di.productCommonDomainModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createProductPresentationModule = module {
    includes(
        createProductDomainModule,
        productCommonDomainModule,
    )
    viewModelOf(::CreateProductViewModel)
}