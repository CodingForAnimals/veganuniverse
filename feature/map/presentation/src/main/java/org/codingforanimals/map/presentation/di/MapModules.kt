package org.codingforanimals.map.presentation.di

import org.codingforanimals.map.presentation.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val mapFeatureModule = module {
    viewModel { MapViewModel(get()) }
}

internal fun injectMapModules() = loadKoinModules(
    mapFeatureModule
)