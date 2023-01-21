package org.codingforanimals.veganuniverse.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.codingforanimals.veganuniverse.datastore.VeganUniverseDataStore
import org.codingforanimals.veganuniverse.datastore.VeganUniverseDataStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.veganUniverseDataStore by preferencesDataStore("vegan_universe_data_store")

val dataStoreModule = module {
    single { androidContext().veganUniverseDataStore }
    single<VeganUniverseDataStore> { VeganUniverseDataStoreImpl(get()) }
}