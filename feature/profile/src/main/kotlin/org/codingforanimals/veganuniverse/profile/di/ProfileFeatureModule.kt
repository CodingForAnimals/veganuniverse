package org.codingforanimals.veganuniverse.profile.di

import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val profileFeatureModule = module {
    viewModelOf(::ProfileScreenViewModel)
}