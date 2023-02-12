package org.codingforanimals.veganuniverse.profile.presentation.di

import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { ProfileScreenViewModel() }
}