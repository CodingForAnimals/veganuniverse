package org.codingforanimals.veganuniverse.community.presentation.di

import org.codingforanimals.veganuniverse.community.presentation.CommunityScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val communityModule = module {
    viewModelOf(::CommunityScreenViewModel)
}