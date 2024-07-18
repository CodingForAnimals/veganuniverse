package org.codingforanimals.veganuniverse.product.domain.di

import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_PRODUCT_USE_CASES
import org.codingforanimals.veganuniverse.product.domain.usecase.EditProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.GetLatestProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.GetProductDetail
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductBookmarkUseCases
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductListingUseCases
import org.codingforanimals.veganuniverse.product.domain.usecase.QueryProductsPagingDataFlow
import org.codingforanimals.veganuniverse.product.domain.usecase.ReportProduct
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val productFeatureDomainModule = module {
    factoryOf(::QueryProductsPagingDataFlow)
    factoryOf(::GetLatestProducts)
    factoryOf(::EditProduct)
    factoryOf(::ReportProduct)
    factoryOf(::GetProductDetail)
    factoryOf(::ProductListingUseCases)
    factory {
        ProductBookmarkUseCases(
            profileProductUseCases = get(named(PROFILE_PRODUCT_USE_CASES))
        )
    }

}
