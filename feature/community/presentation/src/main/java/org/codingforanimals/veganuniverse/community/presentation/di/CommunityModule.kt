package org.codingforanimals.veganuniverse.community.presentation.di

import org.codingforanimals.veganuniverse.community.presentation.CommunityScreenViewModel
import org.codingforanimals.veganuniverse.user.userModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val communityModule = module {
    includes(userModule)
    viewModel { CommunityScreenViewModel(get(), get()) }
}