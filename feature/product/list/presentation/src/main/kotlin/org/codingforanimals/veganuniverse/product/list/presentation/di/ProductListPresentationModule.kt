package org.codingforanimals.veganuniverse.product.list.presentation.di

import org.codingforanimals.veganuniverse.commons.user.domain.di.commonsUserDomainModule
import org.codingforanimals.veganuniverse.product.list.domain.di.productListDomainModule
import org.codingforanimals.veganuniverse.product.list.presentation.ProductListViewModel
import org.codingforanimals.veganuniverse.product.list.presentation.components.ProductRowViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val productListPresentationModule = module {
    includes(
        productListDomainModule,
        commonsUserDomainModule,
    )
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductRowViewModel)
}