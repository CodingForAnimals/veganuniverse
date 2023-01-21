package org.codingforanimals.map.presentation.di

import org.codingforanimals.map.presentation.MapViewModel
import org.codingforanimals.veganuniverse.common.permissions.permissionsManagerModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val mapFeatureModule = module {
    includes(permissionsManagerModule)
    viewModel { MapViewModel(get()) }
}

internal fun injectMapModules() = loadKoinModules(
    mapFeatureModule
)