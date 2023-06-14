package org.codingforanimals.veganuniverse.create.presentation.place.di

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.CreatePlaceUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetAutoCompleteIntentUseCase
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetCreatePlaceScreenContent
import org.codingforanimals.veganuniverse.create.presentation.place.usecase.GetPlaceDataUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val createPlaceModule = module {
    factory<PlacesClient> {
        Places.initialize(get(), "AIzaSyATDgK_LBizEIapwfzy90-_BI4tVAyKenE")
        Places.createClient(get())
    }
    factoryOf(::GetAutoCompleteIntentUseCase)
    factoryOf(::GetCreatePlaceScreenContent)
    factoryOf(::GetPlaceDataUseCase)
    factoryOf(::CreatePlaceUseCase)
    viewModelOf(::CreatePlaceViewModel)
}