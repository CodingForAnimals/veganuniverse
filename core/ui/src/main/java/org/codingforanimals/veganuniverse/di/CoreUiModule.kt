package org.codingforanimals.veganuniverse.di

import org.codingforanimals.veganuniverse.shared.ui.grid.StaggeredItemGridViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val coreUiModule = module {
    viewModelOf(::StaggeredItemGridViewModel)
}