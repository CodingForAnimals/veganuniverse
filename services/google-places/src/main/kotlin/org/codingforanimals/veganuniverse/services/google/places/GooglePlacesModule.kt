package org.codingforanimals.veganuniverse.services.google.places

import android.content.Context
import com.google.android.libraries.places.api.Places
import org.codingforanimals.veganuniverse.services.google.places.api.PlacesClient
import org.codingforanimals.veganuniverse.services.google.places.api.PlacesClientImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import com.google.android.libraries.places.api.net.PlacesClient as GooglePlacesClient

val googlePlacesModule = module {
    single { createClient(get()) }
    factoryOf(::PlacesClientImpl) bind PlacesClient::class
}

private fun createClient(context: Context): GooglePlacesClient {
    val apiKey = context.getString(R.string.google_maps_api_key)
    Places.initialize(context, apiKey)
    return Places.createClient(context)
}
