package org.codingforanimals.veganuniverse.create.presentation.place

import android.location.Geocoder
import java.util.Locale
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val createPlaceModule = module {
    factory { Geocoder(get(), Locale.getDefault()) }
    viewModel { CreatePlaceViewModel(get(), get(), get()) }
}