package org.codingforanimals.veganuniverse.additives.presentation.di

import org.codingforanimals.veganuniverse.additives.domain.di.additivesDomainModule
import org.codingforanimals.veganuniverse.additives.presentation.browsing.AdditivesBrowsingViewModel
import org.codingforanimals.veganuniverse.additives.presentation.detail.AdditiveDetailViewModel
import org.codingforanimals.veganuniverse.additives.presentation.detail.edit.AdditiveDetailEditViewModel
import org.codingforanimals.veganuniverse.additives.presentation.validator.detail.AdditiveEditValidationViewModel
import org.codingforanimals.veganuniverse.additives.presentation.validator.list.AdditiveEditListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val additivesPresentationModule = module {
    includes(
        additivesDomainModule,
    )

    viewModelOf(::AdditivesBrowsingViewModel)
    viewModelOf(::AdditiveDetailViewModel)
    viewModelOf(::AdditiveDetailEditViewModel)
    viewModelOf(::AdditiveEditValidationViewModel)
    viewModelOf(::AdditiveEditListViewModel)
}
