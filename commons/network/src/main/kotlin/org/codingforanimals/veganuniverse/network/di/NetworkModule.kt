package org.codingforanimals.veganuniverse.network.di

import android.content.Context
import org.codingforanimals.veganuniverse.network.NetworkUtils
import org.codingforanimals.veganuniverse.network.NetworkUtilsImpl
import org.koin.dsl.module

val networkModule = module {
    factory<NetworkUtils> {
        val appContext: Context = get()
        NetworkUtilsImpl(appContext)
    }
}