package org.codingforanimals.veganuniverse.create.presentation.place.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.CreatePlaceUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetAutoCompleteIntentUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetCreatePlaceScreenContent
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.google.android.libraries.places.api.net.PlacesClient as MapsPlacesClient

internal val createPlaceModule = module {
    factory { PlacesClient.createClient(get()) }
    factoryOf(::GetAutoCompleteIntentUseCase)
    factoryOf(::GetCreatePlaceScreenContent)
    factoryOf(::GetPlaceDataUseCase)
    factoryOf(::CreatePlaceUseCase)
    viewModelOf(::CreatePlaceViewModel)
}

/**
 * Client and places API could be abstracted to the service module
 */
object PlacesClient {
    fun createClient(context: Context): MapsPlacesClient {
        val apiKey = context.getString(R.string.google_maps_api_key)
        Places.initialize(context, apiKey)
        return Places.createClient(context)
    }
}