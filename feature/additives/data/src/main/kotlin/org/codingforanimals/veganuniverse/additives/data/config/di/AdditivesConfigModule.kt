package org.codingforanimals.veganuniverse.additives.data.config.di

import android.content.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.additives.data.config.local.AdditivesConfigDataStore
import org.codingforanimals.veganuniverse.additives.data.config.local.AdditivesConfigLocalStorage
import org.codingforanimals.veganuniverse.additives.data.config.local.additivesConfigDataStore
import org.codingforanimals.veganuniverse.additives.data.config.remote.AdditivesConfigFirebaseSource
import org.codingforanimals.veganuniverse.additives.data.config.remote.AdditivesConfigRemoteSource
import org.koin.dsl.module

internal val additivesConfigModule = module {
    factory<AdditivesConfigRemoteSource> {
        AdditivesConfigFirebaseSource(
            additivesConfigReference = Firebase.database.getReference(AdditivesConfigFirebaseSource.ADDITIVES_CONFIG_PATH)
        )
    }
    factory<AdditivesConfigLocalStorage> {
        val context = get<Context>()
        AdditivesConfigDataStore(
            dataStore = context.additivesConfigDataStore,
        )
    }
}