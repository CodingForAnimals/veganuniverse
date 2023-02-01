package org.codingforanimals.veganuniverse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.codingforanimals.veganuniverse.onboarding.presentation.di.onboardingModule
import org.codingforanimals.veganuniverse.ui.VeganUniverseApp
import org.codingforanimals.veganuniverse.ui.theme.VeganUniverseTheme
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainModules = module {
            includes(onboardingModule)
        }
        loadKoinModules(mainModules)
        setContent {
            VeganUniverseTheme {
                VeganUniverseApp()
            }
        }
    }
}