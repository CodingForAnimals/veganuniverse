package org.codingforanimals.veganuniverse.create.home.persentation.di

import org.codingforanimals.veganuniverse.create.home.persentation.CreateHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createHomePresentationModule = module {
    viewModelOf(::CreateHomeViewModel)
}