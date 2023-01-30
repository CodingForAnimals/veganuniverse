package org.codingforanimals.places.presentation.di

import org.codingforanimals.places.presentation.PlacesViewModel
import org.codingforanimals.veganuniverse.common.dispatcher.coroutineDispatcherModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val placesModule = module {
    includes(coroutineDispatcherModule)
    viewModel { PlacesViewModel(get(), get()) }
}

internal fun injectPlacesModule() = loadKoinModules(placesModule)