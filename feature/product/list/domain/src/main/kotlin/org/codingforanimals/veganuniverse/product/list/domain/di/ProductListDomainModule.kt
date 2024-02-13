package org.codingforanimals.veganuniverse.product.list.domain.di

import org.codingforanimals.veganuniverse.product.list.data.di.productListDataModule
import org.codingforanimals.veganuniverse.product.list.domain.usecase.GetProducts
import org.codingforanimals.veganuniverse.product.list.domain.usecase.UseCases
import org.koin.dsl.module

val productListDomainModule = module {
    includes(productListDataModule)
    factory {
        UseCases(
            getProducts = GetProducts(get())
        )
    }
}
