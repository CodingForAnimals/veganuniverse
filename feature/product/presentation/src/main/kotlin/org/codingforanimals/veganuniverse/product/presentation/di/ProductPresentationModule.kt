package org.codingforanimals.veganuniverse.product.presentation.di

import org.codingforanimals.veganuniverse.commons.user.domain.di.commonsUserDomainModule
import org.codingforanimals.veganuniverse.product.domain.di.productListDomainModule
import org.codingforanimals.veganuniverse.product.presentation.components.ProductAdditionalInfoViewModel
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionDialogViewModel
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel
import org.codingforanimals.veganuniverse.product.presentation.list.ProductListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val productPresentationModule = module {
    includes(
        productListDomainModule,
        commonsUserDomainModule,
    )
    viewModelOf(::ProductHomeViewModel)
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductAdditionalInfoViewModel)
    viewModelOf(::ProductSuggestionDialogViewModel)
}
