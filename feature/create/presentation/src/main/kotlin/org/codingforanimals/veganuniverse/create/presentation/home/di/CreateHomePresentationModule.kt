package org.codingforanimals.veganuniverse.create.presentation.home.di

import org.codingforanimals.veganuniverse.create.presentation.home.CreateHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createHomePresentationModule = module {
    viewModelOf(::CreateHomeViewModel)
}