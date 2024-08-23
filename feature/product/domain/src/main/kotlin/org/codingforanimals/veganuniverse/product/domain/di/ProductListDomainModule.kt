package org.codingforanimals.veganuniverse.product.domain.di

import org.codingforanimals.veganuniverse.product.data.di.productDataModule
import org.codingforanimals.veganuniverse.product.domain.usecase.GetLatestProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.GetPaginatedProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductSuggestionUseCases
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val productListDomainModule = module {
    includes(productDataModule)
    factoryOf(::ProductSuggestionUseCases)
    factory { GetPaginatedProducts(get()) }
    factory { GetLatestProducts(get()) }
}
