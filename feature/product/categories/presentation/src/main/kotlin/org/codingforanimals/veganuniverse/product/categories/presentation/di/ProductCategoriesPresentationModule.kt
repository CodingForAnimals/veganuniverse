package org.codingforanimals.veganuniverse.product.categories.presentation.di

import org.codingforanimals.veganuniverse.product.categories.presentation.ProductCategoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val productCategoriesPresentationModule = module {
    viewModelOf(::ProductCategoriesViewModel)
}