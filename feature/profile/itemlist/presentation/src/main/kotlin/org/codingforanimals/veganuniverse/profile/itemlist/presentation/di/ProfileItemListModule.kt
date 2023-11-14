package org.codingforanimals.veganuniverse.profile.itemlist.presentation.di

import org.codingforanimals.veganuniverse.profile.itemlist.presentation.ProfileItemListViewModel
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.usecase.GetProfileItemsUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileItemListModule = module {
    factoryOf(::GetProfileItemsUseCase)
    viewModelOf(::ProfileItemListViewModel)
}