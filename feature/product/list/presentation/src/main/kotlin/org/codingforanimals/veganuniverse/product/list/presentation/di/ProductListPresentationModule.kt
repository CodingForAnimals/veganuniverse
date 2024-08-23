package org.codingforanimals.veganuniverse.product.list.presentation.di

import org.codingforanimals.veganuniverse.product.list.presentation.ProductListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val productListPresentationModule = module {
    // includes(productListDomainModule)
    viewModelOf(::ProductListViewModel)
}