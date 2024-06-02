package org.codingforanimals.veganuniverse.product.presentation.di

import org.codingforanimals.veganuniverse.product.domain.di.productDomainModule
import org.codingforanimals.veganuniverse.product.domain.di.productListDomainModule
import org.codingforanimals.veganuniverse.product.presentation.components.ProductAdditionalInfoViewModel
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionDialogViewModel
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingViewModel
import org.codingforanimals.veganuniverse.user.domain.di.userDomainModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val productPresentationModule = module {
    includes(
        productListDomainModule,
        userDomainModule,
        productDomainModule,
    )
    viewModelOf(::ProductHomeViewModel)
    viewModelOf(::ProductBrowsingViewModel)
    viewModelOf(::ProductAdditionalInfoViewModel)
    viewModelOf(::ProductSuggestionDialogViewModel)
}
