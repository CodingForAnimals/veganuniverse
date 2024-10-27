package org.codingforanimals.veganuniverse.product.presentation.di

import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.codingforanimals.veganuniverse.commons.user.presentation.di.userCommonPresentationModule
import org.codingforanimals.veganuniverse.product.domain.di.productFeatureDomainModule
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingViewModel
import org.codingforanimals.veganuniverse.product.presentation.edit.EditProductViewModel
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailViewModel
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel
import org.codingforanimals.veganuniverse.product.presentation.listing.ProductListingViewModel
import org.codingforanimals.veganuniverse.product.presentation.validate.CompareProductEditViewModel
import org.codingforanimals.veganuniverse.product.presentation.validate.ValidateProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val productPresentationModule = module {
    includes(
        productFeatureDomainModule,
        userCommonDomainModule,
        userCommonPresentationModule,
    )
    viewModelOf(::ProductHomeViewModel)
    viewModelOf(::ProductBrowsingViewModel)
    viewModelOf(::ProductDetailViewModel)
    viewModelOf(::ProductListingViewModel)
    viewModelOf(::ValidateProductsViewModel)
    viewModelOf(::EditProductViewModel)
    viewModelOf(::CompareProductEditViewModel)
}
