package org.codingforanimals.places.presentation.di

import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.places.presentation.home.PlacesHomeViewModel
import org.codingforanimals.places.presentation.home.state.PlacesHomeSavedStateHandler
import org.codingforanimals.places.presentation.home.usecase.GetPlacesUseCase
import org.codingforanimals.veganuniverse.places.domain.placesDomainModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val placesModule = module {
    includes(placesDomainModule)
    factory { GetPlacesUseCase(get()) }
    single { CameraPositionState() }
    single { PlacesHomeSavedStateHandler() }
    viewModelOf(::PlacesHomeViewModel)

}

fun injectPlacesModule() = loadKoinModules(placesModule)