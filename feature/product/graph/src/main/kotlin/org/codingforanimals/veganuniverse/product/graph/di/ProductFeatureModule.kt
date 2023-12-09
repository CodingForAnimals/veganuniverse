package org.codingforanimals.veganuniverse.product.graph.di

import org.codingforanimals.veganuniverse.product.categories.presentation.di.productCategoriesPresentationModule
import org.codingforanimals.veganuniverse.product.list.presentation.di.productListPresentationModule
import org.koin.dsl.module

val productFeatureModule = module {
    includes(
        productCategoriesPresentationModule,
        productListPresentationModule,
    )
}