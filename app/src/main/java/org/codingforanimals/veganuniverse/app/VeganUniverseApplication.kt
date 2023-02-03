package org.codingforanimals.veganuniverse.app

import android.app.Application
import org.codingforanimals.veganuniverse.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class VeganUniverseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@VeganUniverseApplication)
            loadKoinModules(appModule)
        }
    }
}