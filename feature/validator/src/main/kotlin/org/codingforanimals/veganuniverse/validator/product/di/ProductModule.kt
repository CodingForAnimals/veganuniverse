package org.codingforanimals.veganuniverse.validator.product.di

import org.codingforanimals.veganuniverse.validator.product.domain.GetUnvalidatedProductsPaginationFlowUseCase
import org.codingforanimals.veganuniverse.validator.product.domain.ValidateProductUseCase
import org.codingforanimals.veganuniverse.validator.product.presentation.ValidateProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val productModule = module {
    factoryOf(::GetUnvalidatedProductsPaginationFlowUseCase)
    factoryOf(::ValidateProductUseCase)
    viewModelOf(::ValidateProductsViewModel)
}