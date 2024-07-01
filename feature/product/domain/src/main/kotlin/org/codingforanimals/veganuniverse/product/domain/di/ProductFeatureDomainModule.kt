package org.codingforanimals.veganuniverse.product.domain.di

import org.codingforanimals.veganuniverse.product.domain.usecase.EditProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.GetLatestProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.QueryProductsPagingDataFlow
import org.codingforanimals.veganuniverse.product.domain.usecase.ReportProduct
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val productFeatureDomainModule = module {
    factoryOf(::QueryProductsPagingDataFlow)
    factoryOf(::GetLatestProducts)
    factoryOf(::EditProduct)
    factoryOf(::ReportProduct)

}
