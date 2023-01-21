package org.codingforanimals.veganuniverse.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.codingforanimals.veganuniverse.datastore.VeganUniverseDataStore
import org.codingforanimals.veganuniverse.datastore.VeganUniverseDataStoreImpl

private val Context.veganUniverseDataStore by preferencesDataStore("vegan_universe_data_store")

@Module(includes = [VeganUniverseDataStoreModule.DataStoreModule::class])
@InstallIn(SingletonComponent::class)
abstract class VeganUniverseDataStoreModule {

    @Binds
    @Singleton
    abstract fun bindDataStore(
        impl: VeganUniverseDataStoreImpl
    ): VeganUniverseDataStore

    @Module
    class DataStoreModule {

        @Provides
        @Singleton
        fun providesDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.veganUniverseDataStore
    }
}