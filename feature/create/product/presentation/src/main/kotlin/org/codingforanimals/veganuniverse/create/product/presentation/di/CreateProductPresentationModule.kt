package org.codingforanimals.veganuniverse.create.product.presentation.di

import org.codingforanimals.veganuniverse.create.product.domain.di.createProductDomainModule
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel
import org.codingforanimals.veganuniverse.product.domain.di.productDomainModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createProductPresentationModule = module {
    includes(
        createProductDomainModule,
        productDomainModule,
    )
    viewModelOf(::CreateProductViewModel)
}